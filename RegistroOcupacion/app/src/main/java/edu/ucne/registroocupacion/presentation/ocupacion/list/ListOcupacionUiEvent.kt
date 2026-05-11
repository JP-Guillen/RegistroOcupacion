package edu.ucne.registroocupacion.presentation.ocupacion.list

sealed interface ListOcupacionUiEvent {
    object Load : ListOcupacionUiEvent

    data class Delete(val id: Int) : ListOcupacionUiEvent
    data class ShowMessage(val message: String) : ListOcupacionUiEvent
    object CreateNew : ListOcupacionUiEvent
    data class Edit (val id: Int) : ListOcupacionUiEvent

}