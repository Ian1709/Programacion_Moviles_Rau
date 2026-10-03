package com.tecsup.mibodega.ui.cliente.modelo

/**
 * Modelo de datos para un Pedido confirmado.
 */
data class Pedido(
    val id: String,
    val fecha: String,
    val items: List<ItemCarrito>,
    val subtotal: Double,
    val costoEnvio: Double,
    val total: Double,
    val direccion: String,
    val referencia: String,
    val telefono: String,
    val nombreRecibe: String,
    val tipoEntrega: String,
    val estado: String = "Confirmado"
)
