package edu.ucne.registroocupacion.data.repository

import edu.ucne.registroocupacion.data.local.dao.EmpleadoDao
import edu.ucne.registroocupacion.data.mapper.toDomain
import edu.ucne.registroocupacion.data.mapper.toEntity
import edu.ucne.registroocupacion.domain.Empleados.model.Empleado
import edu.ucne.registroocupacion.domain.Empleados.repository.EmpleadoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class EmpleadoRepositoryImpl @Inject constructor(
    private val Dao: EmpleadoDao
) : EmpleadoRepository {

    override fun ObserverEmpleado(): Flow<List<Empleado>> {
        return Dao.observeAll().map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getEmpleado(id: Int): Empleado? {
        return Dao.getById(id)?.toDomain()
    }

    override suspend fun upsert(empleado: Empleado): Int {
        val entity = empleado.toEntity()
        val result = Dao.Upsert(entity)
        return if (empleado.empleadoId == 0 || empleado.empleadoId == null) result.toInt() else empleado.empleadoId
    }

    override suspend fun delete(id: Int) {
        Dao.deleteById(id)
    }

    override suspend fun getEmpleadoByNombre(nombre: String): List<Empleado> {
        return Dao.getByNombre(nombre).map { it.toDomain() }
    }

    override suspend fun filterByName(nombre: String): Flow<List<Empleado>> = Dao.filterByName(nombre).map { list ->
        list.map { it.toDomain() }
    }

    override suspend fun filterByDate(fecha: LocalDate): Flow<List<Empleado>> = Dao.filterByDate(fecha.toString()).map { list ->
        list.map { it.toDomain() }
    }
}