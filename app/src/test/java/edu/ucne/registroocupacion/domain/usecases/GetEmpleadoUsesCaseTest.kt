package edu.ucne.registroocupacion.domain.Empleados.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.registroocupacion.domain.Empleados.model.Empleado
import edu.ucne.registroocupacion.domain.Empleados.repository.EmpleadoRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@ExperimentalCoroutinesApi
class GetEmpleadoUseCaseTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var getEmpleadoUseCase: GetEmpleadoUseCase
    private lateinit var repository: EmpleadoRepository

    @Before
    fun setup() {
        repository = mockk()
        getEmpleadoUseCase = GetEmpleadoUseCase(repository)
    }

    @Test
    fun `invoke retorna empleado correctamente cuando existe`() = runTest {
        val empleadoId = 1
        val empleadoEsperado = Empleado(
            empleadoId = empleadoId,
            nombres = "James Urena",
            fechaIngreso = LocalDate.of(2026, 5, 17),
            sexo = "M",
            sueldo = 35000.0
        )
        coEvery { repository.getEmpleado(empleadoId) } returns empleadoEsperado

        val result = getEmpleadoUseCase(empleadoId)

        assertNotNull(result)
        assertEquals("James Urena", result?.nombres)
        assertEquals(empleadoId, result?.empleadoId)
        coVerify(exactly = 1) { repository.getEmpleado(empleadoId) }
    }

    @Test
    fun `invoke retorna nulo cuando el empleado no existe`() = runTest {
        val empleadoIdInexistente = 99
        coEvery { repository.getEmpleado(empleadoIdInexistente) } returns null

        val result = getEmpleadoUseCase(empleadoIdInexistente)

        assertNull(result)
        coVerify(exactly = 1) { repository.getEmpleado(empleadoIdInexistente) }
    }

    @Test
    fun `invoke lanza IllegalArgumentException cuando el id es menor o igual a cero`() = runTest {
        val empleadoIdInvalido = 0

        val exception = assertThrows(IllegalArgumentException::class.java) {
            runBlocking {
                getEmpleadoUseCase(empleadoIdInvalido)
            }
        }

        assertEquals("El ID debe ser mayor que 0", exception.message)
        coVerify(exactly = 0) { repository.getEmpleado(any()) }
    }
}