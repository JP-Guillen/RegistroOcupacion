package edu.ucne.registroocupacion.presentation.HoraExtra.List

import edu.ucne.registroocupacion.domain.Empleados.model.Empleado
import edu.ucne.registroocupacion.domain.HoraExtra.model.HoraExtra

data class ListHoraExtraUiState (
    val isLoading: Boolean = false,
    val registros: List<HoraExtra> = emptyList(),
    val empleados: List<Empleado> = emptyList(),
    val error: String? = null
)