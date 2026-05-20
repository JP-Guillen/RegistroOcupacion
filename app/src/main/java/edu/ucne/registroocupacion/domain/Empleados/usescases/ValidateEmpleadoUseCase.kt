package edu.ucne.registroocupacion.domain.Empleados.usecases

import edu.ucne.registroocupacion.domain.Empleados.repository.EmpleadoRepository
import java.time.LocalDate
import javax.inject.Inject

class ValidateEmpleadoUseCase @Inject constructor(
    private val repository: EmpleadoRepository
) {

    data class ValidationResult(
        val isValid: Boolean = false,
        val nombreError: String? = null,
        val fechaError: String? = null,
        val sueldoError: String? = null
    )

    suspend operator fun invoke(
        nombre: String,
        fecha: LocalDate?,
        sueldo: Double,
        currentEmpleadoId: Int? = null
    ): ValidationResult {

        val nombreError = when {
            nombre.isBlank() -> "El nombre no puede estar vacío."
            else -> {
                val existingEmpleado = repository.getEmpleadoByNombre(nombre)
                val isDuplicate = existingEmpleado.any { it.empleadoId != currentEmpleadoId }
                if (isDuplicate) "Ya existe un empleado con este mismo nombre." else null
            }
        }

        val fechaError = when {
            fecha == null -> "La fecha no puede estar vacía."
            else -> null
        }

        val sueldoError = when {
            sueldo == null -> "El sueldo debe ser un número válido."
            sueldo <= 0.0 -> "El sueldo debe ser mayor que 0."
            else -> null
        }

        return ValidationResult(
            isValid = nombreError == null && fechaError == null && sueldoError == null,
            nombreError = nombreError,
            fechaError = fechaError,
            sueldoError = sueldoError
        )
    }
}