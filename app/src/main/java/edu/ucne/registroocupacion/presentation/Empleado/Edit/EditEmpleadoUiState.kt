package edu.ucne.registroocupacion.presentation.empleado.edit

import java.time.LocalDate

data class EditEmpleadoUIState(
    val empleadoId: Int? = null,
    val nombre: String = "",
    val sueldo: Double = 0.0,
    val sexo: String = "",
    val fechaIngreso: LocalDate? = null,
    val sexoError: String? = null,
    val nombreError: String? = null,
    val sueldoError: String? = null,
    val fechaIngresoError: String? = null,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val isNew: Boolean = true,
    val saved: Boolean = false,
    val deleted: Boolean = false
)