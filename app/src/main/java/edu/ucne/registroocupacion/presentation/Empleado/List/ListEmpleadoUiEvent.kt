package edu.ucne.registroocupacion.presentation.empleado.list

sealed interface ListEmpleadoUiEvent {
    object Load : ListEmpleadoUiEvent
    data class Delete(val id: Int) : ListEmpleadoUiEvent
    data class ShowMessage(val message: String) : ListEmpleadoUiEvent
    object CreateNew : ListEmpleadoUiEvent
    data class Edit(val id: Int) : ListEmpleadoUiEvent
}