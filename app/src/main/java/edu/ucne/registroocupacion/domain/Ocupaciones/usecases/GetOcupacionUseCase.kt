package edu.ucne.registroocupacion.domain.Ocupaciones.usecases

import edu.ucne.registroocupacion.domain.Ocupaciones.model.Ocupacion
import edu.ucne.registroocupacion.domain.Ocupaciones.repository.OcupacionRepository
import javax.inject.Inject

class GetOcupacionUseCase @Inject constructor(
    private val repository: OcupacionRepository
) {
    suspend operator fun invoke(id: Int): Ocupacion?{
        if (id <= 0) throw IllegalArgumentException("El ID debe ser mayor que 0")
        return repository.getOcupacion(id)
    }
}