package edu.ucne.registroocupacion.domain.Empleados.repository

import edu.ucne.registroocupacion.domain.Empleados.model.Empleado
import edu.ucne.registroocupacion.domain.Ocupaciones.model.Ocupacion
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface EmpleadoRepository {
    fun ObserverEmpleado(): Flow<List<Empleado>>
    suspend fun getEmpleado(id: Int): Empleado?
    suspend fun upsert(empleado: Empleado): Int
    suspend fun delete(id: Int)
    suspend fun filterByName(nombre: String): Flow<List<Empleado>>
    suspend fun filterByDate(fecha: LocalDate): Flow<List<Empleado>>
    suspend fun getEmpleadoByNombre(nombre: String): List<Empleado>
}