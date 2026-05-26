package edu.ucne.registroocupacion.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "empleado")
data class EmpleadoEntity(
    @PrimaryKey(autoGenerate = true)
    val empleadoID : Int = 0,
    val nombres: String,
    val sexo: String,
    val sueldo: Double = 0.0,
    val fechaIngreso : String

)
