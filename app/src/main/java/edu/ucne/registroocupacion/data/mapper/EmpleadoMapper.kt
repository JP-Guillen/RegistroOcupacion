package edu.ucne.registroocupacion.data.mapper

import edu.ucne.registroocupacion.data.local.entities.EmpleadoEntity
import edu.ucne.registroocupacion.domain.Empleados.model.Empleado
import java.time.LocalDate

fun EmpleadoEntity.toDomain(): Empleado =
    Empleado(
        empleadoId = empleadoID,
        nombres = nombres,
        sueldo = sueldo,
        sexo = sexo,
        fechaIngreso = LocalDate.parse(fechaIngreso)
    )

fun Empleado.toEntity(): EmpleadoEntity =
    EmpleadoEntity(
        empleadoID = empleadoId,
        nombres = nombres,
        sueldo = sueldo,
        sexo = sexo,
        fechaIngreso = fechaIngreso.toString()
    )