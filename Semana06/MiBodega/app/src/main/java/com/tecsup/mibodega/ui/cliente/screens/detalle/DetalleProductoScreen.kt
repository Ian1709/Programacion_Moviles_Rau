package com.tecsup.mibodega.ui.cliente.screens.detalle

import androidx.compose.runtime.Composable
import com.tecsup.mibodega.ui.cliente.modelo.Producto

/**
 * Delegado de compatibilidad para DetalleProductoScreen.
 */
@Composable
fun DetalleProductoScreen(
    producto: Producto,
    onVolver: () -> Unit,
    onAgregarAlCarrito: (Producto, Int) -> Unit
) {
    PantallaDetalleProducto(
        producto = producto,
        onVolver = onVolver,
        onAgregarAlCarrito = onAgregarAlCarrito
    )
}
