package edu.ucne.registroocupacion.presentation.HoraExtra.Edit

import edu.ucne.registroocupacion.domain.Empleados.model.Empleado

sealed interface EditHoraExtraUiEvent {
    data class Load(val id: Int?) : EditHoraExtraUiEvent
    data class EmpleadoChanged(val empleado: Empleado) : EditHoraExtraUiEvent
    data class HorasSemanalesChanged(val value: String) : EditHoraExtraUiEvent
    data class HorasNocturnasChanged(val value: String) : EditHoraExtraUiEvent
    object Save : EditHoraExtraUiEvent
    object Delete : EditHoraExtraUiEvent
}