package edu.ucne.registroocupacion.domain.HoraExtra.usecases

import edu.ucne.registroocupacion.domain.HoraExtra.model.HoraExtra
import edu.ucne.registroocupacion.domain.HoraExtra.repository.HoraExtraRepository
import javax.inject.Inject

class DeleteHoraExtraUseCase @Inject constructor(
    private val repository: HoraExtraRepository
) {
    suspend operator fun invoke(horaExtra: HoraExtra) = repository.deleteHoraExtra(horaExtra)
}