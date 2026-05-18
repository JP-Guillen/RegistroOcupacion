package edu.ucne.registroocupacion.domain.Ocupaciones.model

data class Ocupacion(
    val ocupacionId: Int = 0,
    val descripcion:String,
    val sueldo: Double =0.0
)
