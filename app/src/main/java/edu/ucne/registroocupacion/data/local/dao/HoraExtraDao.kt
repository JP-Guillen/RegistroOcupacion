package edu.ucne.registroocupacion.data.local.dao

import edu.ucne.registroocupacion.data.local.entities.HoraExtraEntity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HoraExtraDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHoraExtra(horaExtraEntity: HoraExtraEntity)

    @Update
    suspend fun updateHoraExtra(horaExtraEntity: HoraExtraEntity)

    @Delete
    suspend fun deleteHoraExtra(horaExtraEntity: HoraExtraEntity)

    @Query("SELECT * FROM horas_extras WHERE horaExtraId = :id")
    suspend fun getHoraExtraById(id: Int): HoraExtraEntity?

    @Query("SELECT * FROM horas_extras WHERE empleadoId = :empleadoId ORDER BY fechaRegistro DESC")
    fun getHorasExtrasByEmpleado(empleadoId: Int): Flow<List<HoraExtraEntity>>

    @Query("SELECT * FROM horas_extras ORDER BY fechaRegistro DESC")
    fun getAllHorasExtras(): Flow<List<HoraExtraEntity>>
}