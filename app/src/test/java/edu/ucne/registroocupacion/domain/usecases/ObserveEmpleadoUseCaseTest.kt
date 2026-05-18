package edu.ucne.registroocupacion.domain.Empleados.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.registroocupacion.domain.Empleados.model.Empleado
import edu.ucne.registroocupacion.domain.Empleados.repository.EmpleadoRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@ExperimentalCoroutinesApi
class ObserveEmpleadoUseCaseTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var observeEmpleadoUseCase: ObserveEmpleadoUseCase
    private lateinit var repository: EmpleadoRepository

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        observeEmpleadoUseCase = ObserveEmpleadoUseCase(repository)
    }

    @Test
    fun `debe retornar flow de lista de empleados correctamente`() = runTest {
        val empleados = listOf(
            Empleado(
                empleadoId = 1,
                nombres = "Empleado 1",
                fechaIngreso = LocalDate.of(2026, 5, 17),
                sexo = "M",
                sueldo = 30000.0
            ),
            Empleado(
                empleadoId = 2,
                nombres = "Empleado 2",
                fechaIngreso = LocalDate.of(2026, 5, 17),
                sexo = "F",
                sueldo = 45000.0
            )
        )
        every { repository.ObserverEmpleado() } returns flowOf(empleados)

        val result = observeEmpleadoUseCase().first()

        assertEquals(2, result.size)
        assertEquals("Empleado 1", result[0].nombres)
        assertEquals("Empleado 2", result[1].nombres)
        verify(exactly = 1) { repository.ObserverEmpleado() }
    }
}