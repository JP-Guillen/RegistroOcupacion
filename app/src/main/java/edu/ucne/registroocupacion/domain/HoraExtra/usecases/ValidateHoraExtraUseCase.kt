package edu.ucne.registroocupacion.domain.HoraExtra.usecases

import jakarta.inject.Inject

class ValidateHoraExtraUseCase @Inject constructor() {

    data class ValidationResult(
        val isValid: Boolean,
        val empleadoError: String? = null,
        val horasSemanalesError: String? = null,
        val horasNocturnasError: String? = null
    )

    operator fun invoke(
        empleadoId: Int,
        horasSemanales: Double?,
        horasNocturnas: Double?
    ): ValidationResult {
        val empleadoError = if (empleadoId <= 0) "Debe seleccionar un empleado" else null

        val horasSemanalesError = when {
            horasSemanales == null -> "La cantidad de horas es obligatoria"
            horasSemanales <= 0 -> "Las horas deben ser mayores a 0"
            horasSemanales > 168 -> "No puede exceder las 168 horas semanales"
            else -> null
        }

        val horasNocturnasError = when {
            horasNocturnas == null -> "Las horas nocturnas no pueden ser nulas"
            horasNocturnas < 0 -> "Las horas nocturnas no pueden ser negativas"
            horasSemanales != null && horasNocturnas > horasSemanales -> "No pueden haber más horas nocturnas que horas semanales"
            else -> null
        }

        return ValidationResult(
            isValid = empleadoError == null && horasSemanalesError == null && horasNocturnasError == null,
            empleadoError = empleadoError,
            horasSemanalesError = horasSemanalesError,
            horasNocturnasError = horasNocturnasError
        )
    }
}