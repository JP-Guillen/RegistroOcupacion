package edu.ucne.registroocupaciones.data.repository

import edu.ucne.registroocupacion.data.local.dao.OcupacionDao
import edu.ucne.registroocupacion.data.mapper.toDomain
import edu.ucne.registroocupacion.data.mapper.toEntity
import edu.ucne.registroocupacion.domain.model.Ocupacion
import edu.ucne.registroocupacion.domain.repository.OcupacionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class OcupacionRepositoryImpl @Inject constructor(
    private val ocupacionDao: OcupacionDao
) : OcupacionRepository {

    override fun observeOcupaciones(): Flow<List<Ocupacion>> {
        return ocupacionDao.observeAll().map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getOcupacion(id: Int): Ocupacion? {
        return ocupacionDao.getById(id)?.toDomain()
    }

    override suspend fun upsert(ocupacion: Ocupacion): Int {
        val entity = ocupacion.toEntity()
        val result = ocupacionDao.upsert(entity)
        return if (ocupacion.ocupacionId == 0) result.toInt() else ocupacion.ocupacionId
    }

    override suspend fun delete(id: Int) {
        ocupacionDao.deleteById(id)
    }

    override suspend fun getOcupacionByDescripcion(descripcion: String): List<Ocupacion> {
        return ocupacionDao.getByDescripcion(descripcion).map { it.toDomain() }
    }
}