package com.rau.cafecampus.data.repository

import com.rau.cafecampus.data.local.CafeDatabase
import com.rau.cafecampus.data.local.entity.DetallePedido
import com.rau.cafecampus.data.local.entity.Pedido
import com.rau.cafecampus.data.local.entity.Producto
import com.rau.cafecampus.data.local.entity.Usuario
import com.rau.cafecampus.data.local.relation.PedidoConDetalle
import kotlinx.coroutines.flow.Flow

class CafeRepositoryRoom(
    private val database: CafeDatabase
) : CafeRepository {
    private val usuarioDao = database.usuarioDao()
    private val productoDao = database.productoDao()
    private val pedidoDao = database.pedidoDao()

    override suspend fun login(correo: String, clave: String): Usuario? {
        return usuarioDao.login(correo, clave)
    }

    override suspend fun registrarUsuario(usuario: Usuario): Long {
        return usuarioDao.insertar(usuario)
    }

    override suspend fun obtenerUsuarioPorCorreo(correo: String): Usuario? {
        return usuarioDao.obtenerPorCorreo(correo)
    }

    override fun obtenerUsuarioPorId(id: Int): Flow<Usuario?> {
        return usuarioDao.obtenerPorId(id)
    }

    override fun obtenerProductosDisponibles(): Flow<List<Producto>> {
        return productoDao.obtenerTodosDisponibles()
    }

    override fun obtenerProductosPorCategoria(categoria: String): Flow<List<Producto>> {
        return productoDao.obtenerPorCategoria(categoria)
    }

    override suspend fun obtenerProductoPorId(id: Int): Producto? {
        return productoDao.obtenerPorId(id)
    }

    override suspend fun insertarProductos(productos: List<Producto>) {
        productoDao.insertarTodos(productos)
    }

    override suspend fun crearPedido(pedido: Pedido, detalles: List<DetallePedido>): Long {
        return pedidoDao.insertarPedidoConDetalles(pedido, detalles)
    }

    override fun obtenerPedidosPorCliente(clienteId: Int): Flow<List<PedidoConDetalle>> {
        return pedidoDao.obtenerPedidosPorCliente(clienteId)
    }

    override fun obtenerTodosLosPedidos(): Flow<List<PedidoConDetalle>> {
        return pedidoDao.obtenerTodosLosPedidos()
    }

    override suspend fun actualizarEstadoPedido(pedidoId: Long, nuevoEstado: String) {
        pedidoDao.actualizarEstado(pedidoId, nuevoEstado)
    }
}