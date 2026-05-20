package edu.ucne.registroocupacion.presentation.empleado.edit

import java.time.LocalDate

sealed interface EditEmpleadoUIEvent {
    data class Load(val id: Int?) : EditEmpleadoUIEvent
    data class NombreChanged(val value: String) : EditEmpleadoUIEvent
    data class SueldoChanged(val value: Double = 0.0) : EditEmpleadoUIEvent
    data class SexoChanged(val value: String) : EditEmpleadoUIEvent
    data class FechaIngresoChanged(val value: LocalDate?) : EditEmpleadoUIEvent
    data object Save : EditEmpleadoUIEvent
    data object Delete : EditEmpleadoUIEvent
}