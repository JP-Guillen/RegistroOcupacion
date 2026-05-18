package edu.ucne.registroocupacion.domain.Empleados.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.registroocupacion.domain.Empleados.repository.EmpleadoRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DeleteEmpleadoUseCaseTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var deleteEmpleadoUseCase: DeleteEmpleadoUseCase
    private lateinit var repository: EmpleadoRepository

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        deleteEmpleadoUseCase = DeleteEmpleadoUseCase(repository)
    }

    @Test
    fun `debe llamar al repositorio cuando el id es valido`() = runTest {
        val empleadoIdValido = 1
        coEvery { repository.delete(empleadoIdValido) } just Runs

        deleteEmpleadoUseCase(empleadoIdValido)

        coVerify(exactly = 1) { repository.delete(empleadoIdValido) }
    }

    @Test
    fun `debe lanzar IllegalArgumentException cuando el id es menor o igual a cero`() = runTest {
        val empleadoIdInvalido = 0

        val exception = assertThrows(IllegalArgumentException::class.java) {
            runBlocking {
                deleteEmpleadoUseCase(empleadoIdInvalido)
            }
        }

        assertEquals("El ID debe ser mayor que 0", exception.message)

        coVerify(exactly = 0) { repository.delete(any()) }
    }
}