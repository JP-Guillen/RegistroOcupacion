package edu.ucne.registroocupacion.presentation.HoraExtra.Edit

import edu.ucne.registroocupacion.domain.Empleados.model.Empleado
import edu.ucne.registroocupacion.domain.HoraExtra.model.HoraExtra

data class EditHoraExtraUiState(
    val isNew: Boolean = true,
    val horaExtraId: Int = 0,
    val empleadoId: Int = 0,
    val horasSemanales: String = "",
    val horasNocturnas: String = "",
    val empleados: List<Empleado> = emptyList(),
    val empleadoSeleccionado: Empleado? = null,
    val calculoActual: HoraExtra? = null,
    val horasSemanalesError: String? = null,
    val empleadoError: String? = null,
    val isSaving: Boolean = false,
    val saved: Boolean = false,
    val isDeleting: Boolean = false,
    val deleted: Boolean = false
)