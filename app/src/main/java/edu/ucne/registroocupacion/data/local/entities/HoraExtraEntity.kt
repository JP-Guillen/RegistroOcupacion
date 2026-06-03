package edu.ucne.registroocupacion.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "horas_extras",
    foreignKeys = [
        ForeignKey(
            entity = EmpleadoEntity::class,
            parentColumns = ["empleadoID"],
            childColumns = ["empleadoId"],
            onDelete = ForeignKey.CASCADE
        )],
    indices = [Index(value = ["empleadoId"])]
)
data class HoraExtraEntity(
@PrimaryKey(autoGenerate = true)
val horaExtraId: Int = 0,
val empleadoId: Int,
val horasTrabajadasSemanales: Double,
val horasNormales: Double,
val horasA35: Double,
val horasA100: Double,
val horasNocturnas: Double,
val montoTotalHorasExtras: Double,
val fechaRegistro: String

)