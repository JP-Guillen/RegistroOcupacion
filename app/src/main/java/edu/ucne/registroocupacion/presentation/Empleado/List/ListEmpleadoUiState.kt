package edu.ucne.registroocupacion.presentation.empleado.list

import edu.ucne.registroocupacion.domain.Empleados.model.Empleado

data class ListEmpleadoUiState(
    val isLoading: Boolean = false,
    val empleados: List<Empleado> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null
)