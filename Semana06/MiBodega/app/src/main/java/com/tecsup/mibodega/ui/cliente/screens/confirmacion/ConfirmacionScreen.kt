package com.tecsup.mibodega.ui.cliente.screens.confirmacion

import androidx.compose.runtime.Composable
import com.tecsup.mibodega.ui.cliente.modelo.Pedido

/**
 * Delegado de compatibilidad para ConfirmacionScreen.
 */
@Composable
fun ConfirmacionScreen(
    pedido: Pedido?,
    onVerMisPedidos: () -> Unit,
    onVolverAlInicio: () -> Unit
) {
    PantallaConfirmacion(
        pedido = pedido,
        onVerMisPedidos = onVerMisPedidos,
        onVolverAlInicio = onVolverAlInicio
    )
}
