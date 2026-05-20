package edu.ucne.registroocupacion.domain.Empleados.usecases

import edu.ucne.registroocupacion.domain.Empleados.repository.EmpleadoRepository
import javax.inject.Inject

class DeleteEmpleadoUseCase @Inject constructor(
    private val repository: EmpleadoRepository
){
    suspend operator fun invoke(id: Int) {
        if(id <= 0 )throw IllegalArgumentException("El ID debe ser mayor que 0")
        repository.delete(id)
    }
}