package edu.ucne.registroocupacion.domain.Empleados.usecases

import edu.ucne.registroocupacion.domain.Empleados.model.Empleado
import edu.ucne.registroocupacion.domain.Empleados.repository.EmpleadoRepository
import edu.ucne.registroocupacion.domain.Ocupaciones.model.Ocupacion
import javax.inject.Inject

class GetEmpleadoUseCase @Inject constructor(
    private val repository: EmpleadoRepository
){
    suspend operator fun invoke(id: Int): Empleado?{
        if (id <= 0) throw IllegalArgumentException("El ID debe ser mayor que 0")
        return repository.getEmpleado(id)
    }
}