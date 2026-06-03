package edu.ucne.registroocupacion.domain.HoraExtra.usecases

import edu.ucne.registroocupacion.domain.HoraExtra.model.HoraExtra
import edu.ucne.registroocupacion.domain.HoraExtra.repository.HoraExtraRepository
import javax.inject.Inject

class UpsertHoraExtraUseCase @Inject constructor(
    private val repository: HoraExtraRepository,
    private val validate: ValidateHoraExtraUseCase
){
    suspend operator fun invoke(horaExtra: HoraExtra): Result<Unit>{
        val validation = validate(
            empleadoId = horaExtra.empleadoId,
            horasSemanales = horaExtra.horasTrabajadasSemanales,
            horasNocturnas = horaExtra.horasNocturnas
        )
        return if (validation.isValid){
            repository.saveHoraExtra(horaExtra)
            Result.success(Unit)
        }else{
            val error = validation.empleadoError
                ?: validation.horasSemanalesError
                ?: validation.horasNocturnasError
                ?: "Error de validation"
            Result.failure(Exception(error))
        }
    }
}