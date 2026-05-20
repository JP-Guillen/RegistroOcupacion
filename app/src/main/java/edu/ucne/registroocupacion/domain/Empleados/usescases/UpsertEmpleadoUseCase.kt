package edu.ucne.registroocupacion.domain.Empleados.usecases

import edu.ucne.registroocupacion.domain.Empleados.model.Empleado
import edu.ucne.registroocupacion.domain.Empleados.repository.EmpleadoRepository
import javax.inject.Inject

class UpsertEmpleadoUseCase @Inject constructor(
    private val repository: EmpleadoRepository,
    private val validateEmpleadoUseCase: ValidateEmpleadoUseCase
) {
    suspend operator fun invoke(empleado: Empleado): Result<Int> {
        return try {
            val validation = validateEmpleadoUseCase(
                nombre = empleado.nombres,
                sueldo = empleado.sueldo,
                fecha = empleado.fechaIngreso,
                currentEmpleadoId = if (empleado.empleadoId != 0) empleado.empleadoId else null
            )
            if (!validation.isValid) {
                val errorMsg = validation.nombreError ?: validation.sueldoError ?: validation.fechaError ?: "Error de Validación"
                Result.failure(IllegalArgumentException(errorMsg))
            } else {
                val id = repository.upsert(empleado)
                Result.success(id)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}