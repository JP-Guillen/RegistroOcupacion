package edu.ucne.registroocupacion.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registroocupacion.data.local.entities.EmpleadoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmpleadoDao {
    @Query("SELECT * FROM empleado ORDER BY empleadoID DESC")
    fun observeAll(): Flow<List<EmpleadoEntity>>

    @Query("SELECT * FROM empleado WHERE empleadoID = :id")
    suspend fun getById(id: Int): EmpleadoEntity?

    @Upsert
    suspend fun Upsert(entity: EmpleadoEntity):Long

    @Delete
    suspend fun delete(entity: EmpleadoEntity)

    @Query("DELETE FROM empleado WHERE empleadoID = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM empleado WHERE nombres = :nombre")
    suspend fun getByNombre(nombre: String): List<EmpleadoEntity>

    @Query("SELECT * FROM empleado WHERE nombres LIKE '%' || :nombre || '%' ")
    fun filterByName(nombre: String): Flow<List<EmpleadoEntity>>

    @Query("SELECT * FROM empleado WHERE fechaIngreso = :fecha")
    fun filterByDate(fecha: String): Flow<List<EmpleadoEntity>>
}