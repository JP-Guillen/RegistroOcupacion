package edu.ucne.registroocupacion.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.registroocupacion.data.local.dao.EmpleadoDao
import edu.ucne.registroocupacion.data.local.entities.EmpleadoEntity
import edu.ucne.registroocupacion.domain.Empleados.model.Empleado
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
class EmpleadoRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: EmpleadoRepositoryImpl
    private lateinit var dao: EmpleadoDao

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        repository = EmpleadoRepositoryImpl(dao)
    }

    @Test
    fun `upsert guarda nuevo empleado correctamente y retorna id generado`() = runTest {
        // Given
        val empleadoNuevo = Empleado(
            empleadoId = 0,
            nombres = "Nuevo Empleado",
            fechaIngreso = LocalDate.of(2026, 5, 17),
            sexo = "M",
            sueldo = 35000.0
        )
        coEvery { dao.Upsert(any()) } returns 5L

        // When
        val resultId = repository.upsert(empleadoNuevo)

        // Then
        assertEquals(5, resultId)
        coVerify { dao.Upsert(any()) }
    }

    @Test
    fun `upsert actualiza empleado existente correctamente y retorna su mismo id`() = runTest {
        // Given
        val empleadoExistente = Empleado(
            empleadoId = 1,
            nombres = "Empleado Actualizado",
            fechaIngreso = LocalDate.of(2026, 5, 17),
            sexo = "M",
            sueldo = 45000.0
        )
        coEvery { dao.Upsert(any()) } returns 1L

        // When
        val resultId = repository.upsert(empleadoExistente)

        // Then
        assertEquals(1, resultId)
        coVerify { dao.Upsert(any()) }
    }

    @Test
    fun `delete elimina empleado por id correctamente`() = runTest {
        // Given
        val empleadoIdAEliminar = 12
        coEvery { dao.deleteById(empleadoIdAEliminar) } just Runs

        // When
        repository.delete(empleadoIdAEliminar)

        // Then
        coVerify { dao.deleteById(empleadoIdAEliminar) }
    }

    @Test
    fun `ObserverEmpleado retorna flow de lista de empleados mapeados al dominio`() = runTest {
        // Given
        val listaEntities = listOf(
            EmpleadoEntity(
                empleadoID = 1,
                nombres = "Empleado Uno",
                fechaIngreso = "2026-05-17",
                sexo = "M",
                sueldo = 30000.0
            ),
            EmpleadoEntity(
                empleadoID = 2,
                nombres = "Empleado Dos",
                fechaIngreso = "2026-05-18",
                sexo = "F",
                sueldo = 42000.0
            )
        )
        every { dao.observeAll() } returns flowOf(listaEntities)

        // When
        val result = repository.ObserverEmpleado().first()

        // Then
        assertEquals(2, result.size)
        assertEquals("Empleado Uno", result[0].nombres)
        assertEquals("Empleado Dos", result[1].nombres)
        assertEquals(LocalDate.of(2026, 5, 17), result[0].fechaIngreso)
        assertEquals(LocalDate.of(2026, 5, 18), result[1].fechaIngreso)
    }

    @Test
    fun `getEmpleado retorna un empleado mapeado correctamente de la base de datos si existe`() = runTest {
        // Given
        val entity = EmpleadoEntity(
            empleadoID = 1,
            nombres = "Empleado Test",
            fechaIngreso = "2026-05-17",
            sexo = "M",
            sueldo = 35000.0
        )
        coEvery { dao.getById(1) } returns entity

        // When
        val result = repository.getEmpleado(1)

        // Then
        assertNotNull(result)
        assertEquals("Empleado Test", result?.nombres)
        assertEquals(35000.0, result?.sueldo ?: 0.0, 0.0)
        assertEquals(LocalDate.of(2026, 5, 17), result?.fechaIngreso)
    }

    @Test
    fun `getEmpleado retorna nulo si el empleado no existe en la base de datos`() = runTest {
        // Given
        coEvery { dao.getById(99) } returns null

        // When
        val result = repository.getEmpleado(99)

        // Then
        assertNull(result)
    }
}