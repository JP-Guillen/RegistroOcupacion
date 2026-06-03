package edu.ucne.registroocupacion.domain.HoraExtra.usecases

import edu.ucne.registroocupacion.domain.HoraExtra.repository.HoraExtraRepository
import javax.inject.Inject

class GetHoraExtraUseCase @Inject constructor(
    private val repository: HoraExtraRepository
) {
    suspend operator fun invoke (id: Int) = repository.getHoraExtraById(id)
}