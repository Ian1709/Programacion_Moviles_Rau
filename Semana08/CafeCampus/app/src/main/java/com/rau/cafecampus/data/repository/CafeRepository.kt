package com.rau.cafecampus.data.repository

import com.rau.cafecampus.data.local.entity.DetallePedido
import com.rau.cafecampus.data.local.entity.Pedido
import com.rau.cafecampus.data.local.entity.Producto
import com.rau.cafecampus.data.local.entity.Usuario
import com.rau.cafecampus.data.local.relation.PedidoConDetalle
import kotlinx.coroutines.flow.Flow

interface CafeRepository {
    suspend fun login(correo: String, clave: String): Usuario?
    suspend fun registrarUsuario(usuario: Usuario): Long
    suspend fun obtenerUsuarioPorCorreo(correo: String): Usuario?
    fun obtenerUsuarioPorId(id: Int): Flow<Usuario?>

    fun obtenerProductosDisponibles(): Flow<List<Producto>>
    fun obtenerProductosPorCategoria(categoria: String): Flow<List<Producto>>
    suspend fun obtenerProductoPorId(id: Int): Producto?
    suspend fun insertarProductos(productos: List<Producto>)

    suspend fun crearPedido(pedido: Pedido, detalles: List<DetallePedido>): Long
    fun obtenerPedidosPorCliente(clienteId: Int): Flow<List<PedidoConDetalle>>
    fun obtenerTodosLosPedidos(): Flow<List<PedidoConDetalle>>
    fun obtenerPedidosValidos(): Flow<List<Pedido>>
    suspend fun actualizarEstadoPedido(pedidoId: Long, nuevoEstado: String)
}
