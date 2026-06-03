package edu.ucne.registroocupacion.data.mapper

import edu.ucne.registroocupacion.data.local.entities.HoraExtraEntity
import edu.ucne.registroocupacion.domain.HoraExtra.model.HoraExtra


fun HoraExtraEntity.toDomain() = HoraExtra(
    horaExtraId = horaExtraId,
    empleadoId = empleadoId,
    horasTrabajadasSemanales = horasTrabajadasSemanales,
    horasNormales = horasNormales,
    horasA35 = horasA35,
    horasA100 = horasA100,
    horasNocturnas = horasNocturnas,
    montoTotalHorasExtras = montoTotalHorasExtras,
    fechaRegistro = fechaRegistro
)

fun HoraExtra.toEntity() = HoraExtraEntity(
    horaExtraId = horaExtraId,
    empleadoId = empleadoId,
    horasTrabajadasSemanales = horasTrabajadasSemanales,
    horasNormales = horasNormales,
    horasA35 = horasA35,
    horasA100 = horasA100,
    horasNocturnas = horasNocturnas,
    montoTotalHorasExtras = montoTotalHorasExtras,
    fechaRegistro = fechaRegistro
)