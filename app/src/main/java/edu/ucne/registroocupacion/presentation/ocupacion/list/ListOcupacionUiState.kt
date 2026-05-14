package edu.ucne.registroocupacion.presentation.ocupacion.list


import edu.ucne.registroocupacion.domain.model.Ocupacion

data class ListOcupacionUiState (
    val isLoading: Boolean =false,
    val ocupaciones: List<Ocupacion> = emptyList(),
    val  message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null
)