package edu.ucne.registroocupacion.domain.usecases

import edu.ucne.registroocupacion.domain.model.Ocupacion
import edu.ucne.registroocupacion.domain.repository.OcupacionRepository
import javax.inject.Inject

class GetOcupacionUseCase @Inject constructor(
    private val repository: OcupacionRepository
) {
    suspend operator fun invoke(id: Int): Ocupacion?{
        if (id <= 0) throw IllegalArgumentException("El ID debe ser mayor que 0")
        return repository.getOcupacion(id)
    }
}