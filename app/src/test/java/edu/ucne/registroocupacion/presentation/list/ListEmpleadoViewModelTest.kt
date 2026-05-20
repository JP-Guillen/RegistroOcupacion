package edu.ucne.registroocupacion.presentation.empleado.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.registroocupacion.domain.Empleados.model.Empleado
import edu.ucne.registroocupacion.domain.Empleados.usecases.DeleteEmpleadoUseCase
import edu.ucne.registroocupacion.domain.Empleados.usecases.ObserveEmpleadoUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.*
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
class ListEmpleadoViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ListEmpleadoViewModel
    private lateinit var observeEmpleadoUseCase: ObserveEmpleadoUseCase
    private lateinit var deleteEmpleadoUseCase: DeleteEmpleadoUseCase
    private val empleadosFlow = MutableSharedFlow<List<Empleado>>()

    @Before
    fun setup() {
        observeEmpleadoUseCase = mockk()
        deleteEmpleadoUseCase = mockk()

        coEvery { observeEmpleadoUseCase() } returns empleadosFlow

        viewModel = ListEmpleadoViewModel(observeEmpleadoUseCase, deleteEmpleadoUseCase)
    }

    @Test
    fun `init ejecuta Load y observa la lista de empleados correctamente`() = runTest {
        val listaEmpleados = listOf(
            Empleado(
                empleadoId = 1,
                nombres = "Juan Pablo",
                fechaIngreso = LocalDate.of(2026, 5, 17),
                sexo = "M",
                sueldo = 35000.0
            )
        )

        empleadosFlow.emit(listaEmpleados)

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(listaEmpleados, state.empleados)
        assertNull(state.message)
    }

    @Test
    fun `onEvent CreateNew cambia estado para navegacion de creacion`() = runTest {
        viewModel.onEvent(ListEmpleadoUiEvent.CreateNew)

        val state = viewModel.state.value
        assertTrue(state.navigateToCreate)
    }

    @Test
    fun `onEvent Edit cambia estado asignando el id correspondiente para navegacion`() = runTest {
        val empleadoId = 5
        viewModel.onEvent(ListEmpleadoUiEvent.Edit(empleadoId))

        val state = viewModel.state.value
        assertEquals(empleadoId, state.navigateToEditId)
    }

    @Test
    fun `onEvent Delete elimina exitosamente y dispara mensaje de confirmacion`() = runTest {
        val empleadoId = 1
        coEvery { deleteEmpleadoUseCase(empleadoId) } returns Unit

        viewModel.onEvent(ListEmpleadoUiEvent.Delete(empleadoId))

        val state = viewModel.state.value
        assertEquals("Empleado eliminado", state.message)
        coVerify(exactly = 1) { deleteEmpleadoUseCase(empleadoId) }
    }

    @Test
    fun `onEvent Delete maneja excepcion y notifica el error ocurrido`() = runTest {
        val empleadoId = 1
        val mensajeError = "Error de conexion"
        coEvery { deleteEmpleadoUseCase(empleadoId) } throws Exception(mensajeError)

        viewModel.onEvent(ListEmpleadoUiEvent.Delete(empleadoId))

        val state = viewModel.state.value
        assertEquals("Error al eliminar: $mensajeError", state.message)
        coVerify(exactly = 1) { deleteEmpleadoUseCase(empleadoId) }
    }

    @Test
    fun `onNavigationHandled reestablece estados de navegacion y mensajes a valores nulos`() = runTest {
        viewModel.onEvent(ListEmpleadoUiEvent.CreateNew)
        viewModel.onEvent(ListEmpleadoUiEvent.Edit(3))

        viewModel.onNavigationHandled()

        val state = viewModel.state.value
        assertFalse(state.navigateToCreate)
        assertNull(state.navigateToEditId)
        assertNull(state.message)
    }
}