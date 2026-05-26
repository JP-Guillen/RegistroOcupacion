package edu.ucne.registroocupacion.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registroocupacion.data.local.dao.EmpleadoDao
import edu.ucne.registroocupacion.data.local.dao.HoraExtraDao
import edu.ucne.registroocupacion.data.local.dao.OcupacionDao
import edu.ucne.registroocupacion.data.local.entities.EmpleadoEntity
import edu.ucne.registroocupacion.data.local.entities.HoraExtraEntity
import edu.ucne.registroocupacion.data.local.entities.OcupacionEntity

@Database(
    entities = [
        OcupacionEntity::class,
        EmpleadoEntity::class,
        HoraExtraEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class OcupacionDb: RoomDatabase(){
    abstract fun ocupacionDao(): OcupacionDao
    abstract fun EmpleadoDao(): EmpleadoDao
    abstract fun HoraExtraDao(): HoraExtraDao
}