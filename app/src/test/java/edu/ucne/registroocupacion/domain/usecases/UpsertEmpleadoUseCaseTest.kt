package edu.ucne.registroocupacion.domain.Empleados.usecases

import edu.ucne.registroocupacion.domain.Empleados.model.Empleado
import edu.ucne.registroocupacion.domain.Empleados.repository.EmpleadoRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@ExperimentalCoroutinesApi
class UpsertEmpleadoUseCaseTest {

    private lateinit var useCase: UpsertEmpleadoUseCase
    private lateinit var repository: EmpleadoRepository
    private lateinit var validator: ValidateEmpleadoUseCase

    @Before
    fun setup() {
        repository = mockk()
        validator = mockk()
        useCase = UpsertEmpleadoUseCase(repository, validator)
    }

    @Test
    fun `invoke guarda empleado con datos validos`() = runTest {
        val empleado = Empleado(
            empleadoId = 0,
            nombres = "Test Empleado",
            fechaIngreso = LocalDate.of(2026, 5, 17),
            sexo = "M",
            sueldo = 30000.0
        )

        coEvery {
            validator(
                nombre = empleado.nombres,
                sueldo = empleado.sueldo,
                fecha = empleado.fechaIngreso,
                currentEmpleadoId = null
            )
        } returns ValidateEmpleadoUseCase.ValidationResult(isValid = true)

        coEvery { repository.upsert(empleado) } returns 1

        val result = useCase(empleado)

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull())
        coVerify(exactly = 1) { repository.upsert(empleado) }
    }

    @Test
    fun `invoke falla con nombre vacio`() = runTest {
        val empleado = Empleado(
            empleadoId = 0,
            nombres = "",
            fechaIngreso = LocalDate.of(2026, 5, 17),
            sexo = "M",
            sueldo = 30000.0
        )

        coEvery {
            validator(
                nombre = empleado.nombres,
                sueldo = empleado.sueldo,
                fecha = empleado.fechaIngreso,
                currentEmpleadoId = null
            )
        } returns ValidateEmpleadoUseCase.ValidationResult(
            isValid = false,
            nombreError = "El nombre no puede estar vacío."
        )

        val result = useCase(empleado)

        assertTrue(result.isFailure)
        coVerify(exactly = 0) { repository.upsert(any()) }
    }

    @Test
    fun `invoke falla con sueldo invalido`() = runTest {
        val empleado = Empleado(
            empleadoId = 0,
            nombres = "Test Empleado",
            fechaIngreso = LocalDate.of(2026, 5, 17),
            sexo = "M",
            sueldo = -5.0
        )

        coEvery {
            validator(
                nombre = empleado.nombres,
                sueldo = empleado.sueldo,
                fecha = empleado.fechaIngreso,
                currentEmpleadoId = null
            )
        } returns ValidateEmpleadoUseCase.ValidationResult(
            isValid = false,
            sueldoError = "El sueldo debe ser mayor que 0."
        )

        val result = useCase(empleado)

        assertTrue(result.isFailure)
        coVerify(exactly = 0) { repository.upsert(any()) }
    }
}