package com.rau.centrotareas.data

import kotlinx.coroutines.flow.Flow
import com.rau.centrotareas.model.ProgresoPrimos

interface TareasRepository {
    fun progresoTarea(): Flow<Float>
    suspend fun contarPrimos(hasta: Int): Int
    fun contarPrimosConProgreso(hasta: Int): Flow<ProgresoPrimos>
    suspend fun cargarPerfil(): String
    suspend fun cargarPedidos(simularError: Boolean): List<Int>
}
