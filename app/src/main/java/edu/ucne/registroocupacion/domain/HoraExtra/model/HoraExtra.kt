package edu.ucne.registroocupacion.domain.HoraExtra.model

data class HoraExtra (
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