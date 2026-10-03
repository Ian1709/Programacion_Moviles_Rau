package com.tecsup.mibodega.ui.cliente.screens.entrega

import androidx.compose.runtime.Composable

/**
 * Delegado de compatibilidad para DatosEntregaScreen.
 */
@Composable
fun DatosEntregaScreen(
    total: Double = 0.0,
    onVolver: () -> Unit,
    onConfirmarPedido: (direccion: String, referencia: String, telefono: String, nombreRecibe: String) -> Unit
) {
    PantallaDatosEntrega(
        total = total,
        onVolver = onVolver,
        onConfirmarPedido = onConfirmarPedido
    )
}
