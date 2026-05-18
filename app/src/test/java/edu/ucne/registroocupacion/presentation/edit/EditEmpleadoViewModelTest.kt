package edu.ucne.registroocupacion.presentation.empleado.edit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.registroocupacion.domain.Empleados.model.Empleado
import edu.ucne.registroocupacion.domain.Empleados.usecases.DeleteEmpleadoUseCase
import edu.ucne.registroocupacion.domain.Empleados.usecases.GetEmpleadoUseCase
import edu.ucne.registroocupacion.domain.Empleados.usecases.UpsertEmpleadoUseCase
import edu.ucne.registroocupacion.domain.Empleados.usecases.ValidateEmpleadoUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.time.LocalDate

@ExperimentalCoroutinesApi
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

@ExperimentalCoroutinesApi
class EditEmpleadoViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: EditEmpleadoViewModel
    private lateinit var getEmpleadoUseCase: GetEmpleadoUseCase
    private lateinit var upsertEmpleadoUseCase: UpsertEmpleadoUseCase
    private lateinit var deleteEmpleadoUseCase: DeleteEmpleadoUseCase
    private lateinit var validateEmpleadoUseCase: ValidateEmpleadoUseCase

    @Before
    fun setup() {
        getEmpleadoUseCase = mockk()
        upsertEmpleadoUseCase = mockk()
        deleteEmpleadoUseCase = mockk()
        validateEmpleadoUseCase = mockk(relaxed = true)

        viewModel = EditEmpleadoViewModel(
            getEmpleadoUseCase,
            upsertEmpleadoUseCase,
            deleteEmpleadoUseCase,
            validateEmpleadoUseCase
        )
    }

    @Test
    fun `onEvent Load inicializa estado con nuevo empleado cuando el id es nulo o cero`() = runTest {
        viewModel.onEvent(EditEmpleadoUIEvent.Load(0))

        val state = viewModel.state.value
        assertTrue(state.isNew)
        assertNull(state.empleadoId)
    }

    @Test
    fun `onEvent Load carga los datos del empleado correctamente cuando existe`() = runTest {
        val empleadoId = 1
        val empleado = Empleado(
            empleadoId = empleadoId,
            nombres = "Juan Pablo",
            fechaIngreso = LocalDate.of(2026, 5, 17),
            sexo = "M",
            sueldo = 35000.0
        )
        coEvery { getEmpleadoUseCase(empleadoId) } returns empleado

        viewModel.onEvent(EditEmpleadoUIEvent.Load(empleadoId))

        val state = viewModel.state.value
        assertFalse(state.isNew)
        assertEquals(empleadoId, state.empleadoId)
        assertEquals("Juan Pablo", state.nombre)
        assertEquals(35000.0, state.sueldo ?: 0.0, 0.001)
        assertEquals("M", state.sexo)
        assertEquals(LocalDate.of(2026, 5, 17), state.fechaIngreso)
    }

    @Test
    fun `onEvent NombreChanged actualiza el estado y ejecuta validacion`() = runTest {
        val nuevoNombre = "Juan"
        coEvery {
            validateEmpleadoUseCase(
                nombre = nuevoNombre,
                fecha = any(),
                sueldo = any(),
                currentEmpleadoId = any()
            )
        } returns ValidateEmpleadoUseCase.ValidationResult(isValid = true)

        viewModel.onEvent(EditEmpleadoUIEvent.NombreChanged(nuevoNombre))

        val state = viewModel.state.value
        assertEquals(nuevoNombre, state.nombre)
        assertNull(state.nombreError)
        coVerify(exactly = 1) { validateEmpleadoUseCase(nuevoNombre, any(), any(), any()) }
    }

    @Test
    fun `onEvent Save guarda exitosamente cuando la validacion es correcta`() = runTest {
        val fecha = LocalDate.of(2026, 5, 17)

        coEvery {
            validateEmpleadoUseCase(any(), any(), any(), any())
        } returns ValidateEmpleadoUseCase.ValidationResult(isValid = true)

        coEvery { upsertEmpleadoUseCase(any()) } returns Result.success(1)

        viewModel.onEvent(EditEmpleadoUIEvent.NombreChanged("Juan Pablo"))
        viewModel.onEvent(EditEmpleadoUIEvent.SueldoChanged(30000.0))
        viewModel.onEvent(EditEmpleadoUIEvent.SexoChanged("M"))
        viewModel.onEvent(EditEmpleadoUIEvent.FechaIngresoChanged(fecha))

        viewModel.onEvent(EditEmpleadoUIEvent.Save)

        val state = viewModel.state.value
        assertTrue(state.saved)
        assertFalse(state.isSaving)
        assertEquals(1, state.empleadoId)
        coVerify(exactly = 1) { upsertEmpleadoUseCase(any()) }
    }

    @Test
    fun `onEvent Save no guarda y asigna errores si la validacion falla`() = runTest {
        coEvery {
            validateEmpleadoUseCase(any(), any(), any(), any())
        } returns ValidateEmpleadoUseCase.ValidationResult(
            isValid = false,
            nombreError = "El nombre no puede estar vacío."
        )

        viewModel.onEvent(EditEmpleadoUIEvent.Save)

        val state = viewModel.state.value
        assertFalse(state.saved)
        assertEquals("El nombre no puede estar vacío.", state.nombreError)
        coVerify(exactly = 0) { upsertEmpleadoUseCase(any()) }
    }

    @Test
    fun `onEvent Delete elimina el empleado correctamente`() = runTest {
        val empleadoId = 1
        val empleado = Empleado(
            empleadoId = empleadoId,
            nombres = "Juan",
            fechaIngreso = LocalDate.of(2026, 5, 17),
            sexo = "M",
            sueldo = 25000.0
        )
        coEvery { getEmpleadoUseCase(empleadoId) } returns empleado
        coEvery { deleteEmpleadoUseCase(empleadoId) } returns Unit

        viewModel.onEvent(EditEmpleadoUIEvent.Load(empleadoId))
        viewModel.onEvent(EditEmpleadoUIEvent.Delete)

        val state = viewModel.state.value
        assertTrue(state.deleted)
        assertFalse(state.isDeleting)
        coVerify(exactly = 1) { deleteEmpleadoUseCase(empleadoId) }
    }
}