package edu.ucne.registroocupacion.domain.Empleados.usecases

import edu.ucne.registroocupacion.domain.Empleados.model.Empleado
import edu.ucne.registroocupacion.domain.Empleados.repository.EmpleadoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveEmpleadoUseCase @Inject constructor(
    private val repository: EmpleadoRepository
) {
    operator fun invoke(): Flow<List<Empleado>>{
        return repository.ObserverEmpleado()
    }
}