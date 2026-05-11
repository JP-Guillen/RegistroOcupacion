package edu.ucne.registroocupacion.domain.repository

import edu.ucne.registroocupacion.domain.model.Ocupacion
import kotlinx.coroutines.flow.Flow

interface OcupacionRepository {
    fun observeOcupaciones(): Flow<List<Ocupacion>>
    suspend fun getOcupacion(id: Int): Ocupacion?
    suspend fun upsert(Ocupacion: Ocupacion): Int
    suspend fun  delete(id: Int)
    suspend fun getOcupacionByDescripcion(descripcion: String): List<Ocupacion>
}