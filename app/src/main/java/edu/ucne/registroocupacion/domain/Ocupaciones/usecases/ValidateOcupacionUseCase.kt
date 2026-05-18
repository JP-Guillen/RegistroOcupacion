package edu.ucne.registroocupacion.domain.Ocupaciones.usecases

import edu.ucne.registroocupacion.domain.Ocupaciones.repository.OcupacionRepository
import javax.inject.Inject

class ValidateOcupacionUseCase @Inject constructor(
    private val respository: OcupacionRepository
) {

    data class ValidationResult(
        val  isValid: Boolean = false,
        val descripcionError: String? = null,
        val sueldoError: String? = null
    )

    suspend operator fun invoke(
         descripcion: String,
         sueldo: Double?,
         currentOcupacionId: Int? = null
    ): ValidationResult{

        val descripcionError = when {
            descripcion.isBlank() -> "la descripcion no puede estar vacia"
            else -> {
                val existingOcupacion = respository.getOcupacionByDescripcion(descripcion)
                val isDuplicate = existingOcupacion.any() { it.ocupacionId != currentOcupacionId }
                if (isDuplicate) "Ya hay una opcupacion con esta misma descripcion" else null
            }
        }

        val sueldoError = when {
            sueldo == null -> "El sueldo esta vacion"
            sueldo <= 0.0 -> "el sueldo debe ser mayor que 0"
            else -> null
        }
        return ValidationResult(
            isValid = descripcionError == null && sueldoError == null,
             descripcionError = descripcionError,
            sueldoError = sueldoError
        )
    }
}