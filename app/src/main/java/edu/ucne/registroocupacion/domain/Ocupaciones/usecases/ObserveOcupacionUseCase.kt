package edu.ucne.registroocupacion.domain.Ocupaciones.usecases

import edu.ucne.registroocupacion.domain.Ocupaciones.model.Ocupacion
import edu.ucne.registroocupacion.domain.Ocupaciones.repository.OcupacionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveOcupacionUseCase @Inject constructor (
    private  val  repository: OcupacionRepository
){
    operator fun  invoke(): Flow<List<Ocupacion>>{
        return repository.observeOcupaciones()
    }
}