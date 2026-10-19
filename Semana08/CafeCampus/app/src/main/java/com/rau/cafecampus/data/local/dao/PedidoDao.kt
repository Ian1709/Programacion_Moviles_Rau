package com.rau.cafecampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.rau.cafecampus.data.local.entity.DetallePedido
import com.rau.cafecampus.data.local.entity.Pedido
import com.rau.cafecampus.data.local.relation.PedidoConDetalle
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PedidoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertarPedido(pedido: Pedido): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertarDetalles(detalles: List<DetallePedido>)

    @Transaction
    open suspend fun insertarPedidoConDetalles(pedido: Pedido, detalles: List<DetallePedido>): Long {
        val pedidoId = insertarPedido(pedido)
        val detallesConId = detalles.map { it.copy(pedidoId = pedidoId) }
        insertarDetalles(detallesConId)
        return pedidoId
    }

    @Transaction
    @Query("SELECT * FROM pedidos WHERE clienteId = :clienteId ORDER BY fecha DESC")
    abstract fun obtenerPedidosPorCliente(clienteId: Int): Flow<List<PedidoConDetalle>>

    @Transaction
    @Query("SELECT * FROM pedidos ORDER BY fecha DESC")
    abstract fun obtenerTodosLosPedidos(): Flow<List<PedidoConDetalle>>

    @Query("UPDATE pedidos SET estado = :nuevoEstado WHERE id = :pedidoId")
    abstract suspend fun actualizarEstado(pedidoId: Long, nuevoEstado: String)
}
