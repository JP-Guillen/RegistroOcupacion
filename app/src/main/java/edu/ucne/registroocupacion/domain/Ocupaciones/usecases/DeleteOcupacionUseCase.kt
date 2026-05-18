package edu.ucne.registroocupacion.domain.Ocupaciones.usecases

import edu.ucne.registroocupacion.domain.Ocupaciones.repository.OcupacionRepository
import javax.inject.Inject

class DeleteOcupacionUseCase @Inject constructor(
    private val respository : OcupacionRepository
) {
    suspend operator fun invoke(id: Int){
        if(id <= 0 )throw IllegalArgumentException("El ID debe ser mayor que 0")
         respository.delete(id)
    }
}