package edu.ucne.registroocupacion.data.mapper

import edu.ucne.registroocupacion.data.local.entities.OcupacionEntity
import edu.ucne.registroocupacion.domain.Ocupaciones.model.Ocupacion


fun Ocupacion.toEntity(): OcupacionEntity =
    OcupacionEntity(

        ocupacionId = ocupacionId,
        descripcion = descripcion,
        sueldo = sueldo
    )


fun OcupacionEntity.toDomain(): Ocupacion =
    Ocupacion(
        ocupacionId = ocupacionId,
        descripcion = descripcion,
        sueldo = sueldo
    )

