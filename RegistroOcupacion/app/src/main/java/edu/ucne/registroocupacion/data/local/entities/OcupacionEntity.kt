package edu.ucne.registroocupacion.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ocupaciones")
data class OcupacionEntity (
    @PrimaryKey(autoGenerate = true)
    val ocupacionId: Int = 0,
    val descripcion: String,
    val sueldo: Double
)