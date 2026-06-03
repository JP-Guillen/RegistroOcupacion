package edu.ucne.registroocupacion.presentation.HoraExtra.List

import edu.ucne.registroocupacion.domain.HoraExtra.model.HoraExtra

sealed interface ListHoraExtraUiEvent {
    data object Load : ListHoraExtraUiEvent
    data class Delete(val horaExtra: HoraExtra) : ListHoraExtraUiEvent
    data object CreateNew : ListHoraExtraUiEvent
    data class Edit(val id: Int) : ListHoraExtraUiEvent
    data class ShowMessage(val message: String) : ListHoraExtraUiEvent
}