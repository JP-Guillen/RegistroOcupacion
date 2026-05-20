package edu.ucne.registroocupacion.domain.Empleados.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.registroocupacion.domain.Empleados.model.Empleado
import edu.ucne.registroocupacion.domain.Empleados.repository.EmpleadoRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@ExperimentalCoroutinesApi
class ValidateEmpleadoUseCaseTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: ValidateEmpleadoUseCase
    private lateinit var repository: EmpleadoRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = ValidateEmpleadoUseCase(repository)
    }

    @Test
    fun `invoke retorna valido con datos correctos`() = runTest {
        val nombre = "James Urena"
        val fecha = LocalDate.of(2026, 5, 17)
        val sueldo = 25000.0

        coEvery { repository.getEmpleadoByNombre(nombre) } returns emptyList()

        val result = useCase(nombre = nombre, fecha = fecha, sueldo = sueldo)

        assertTrue(result.isValid)
        assertNull(result.nombreError)
        assertNull(result.sueldoError)
        assertNull(result.fechaError)
    }

    @Test
    fun `invoke falla con nombre vacio`() = runTest {
        val nombre = ""
        val fecha = LocalDate.of(2026, 5, 17)
        val sueldo = 25000.0

        val result = useCase(nombre = nombre, fecha = fecha, sueldo = sueldo)

        assertFalse(result.isValid)
        assertEquals("El nombre no puede estar vacío.", result.nombreError)
    }

    @Test
    fun `invoke falla con nombre duplicado`() = runTest {
        val nombre = "James Urena"
        val fecha = LocalDate.of(2026, 5, 17)
        val sueldo = 25000.0
        val empleadoExistente = Empleado(
            empleadoId = 1,
            nombres = "James Urena",
            fechaIngreso = fecha,
            sexo = "M",
            sueldo = 25000.0
        )

        coEvery { repository.getEmpleadoByNombre(nombre) } returns listOf(empleadoExistente)

        val result = useCase(nombre = nombre, fecha = fecha, sueldo = sueldo, currentEmpleadoId = null)

        assertFalse(result.isValid)
        assertEquals("Ya existe un empleado con este mismo nombre.", result.nombreError)
    }

    @Test
    fun `invoke falla con sueldo menor o igual a cero`() = runTest {
        val nombre = "James Urena"
        val fecha = LocalDate.of(2026, 5, 17)
        val sueldo = -5.0

        coEvery { repository.getEmpleadoByNombre(nombre) } returns emptyList()

        val result = useCase(nombre = nombre, fecha = fecha, sueldo = sueldo)

        assertFalse(result.isValid)
        assertEquals("El sueldo debe ser mayor que 0.", result.sueldoError)
    }

    @Test
    fun `invoke falla con fecha nula`() = runTest {
        val nombre = "James Urena"
        val fecha = null
        val sueldo = 25000.0

        coEvery { repository.getEmpleadoByNombre(nombre) } returns emptyList()

        val result = useCase(nombre = nombre, fecha = fecha, sueldo = sueldo)

        assertFalse(result.isValid)
        assertEquals("La fecha no puede estar vacía.", result.fechaError)
    }
}