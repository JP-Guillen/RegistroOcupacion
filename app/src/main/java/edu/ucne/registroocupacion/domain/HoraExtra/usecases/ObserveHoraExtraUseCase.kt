package edu.ucne.registroocupacion.domain.HoraExtra.usecases

import edu.ucne.registroocupacion.domain.HoraExtra.repository.HoraExtraRepository
import javax.inject.Inject

class ObserveHoraExtraUseCase @Inject constructor(
    private val repository: HoraExtraRepository
){
    operator fun invoke() = repository.getAllHorasExtras()
}