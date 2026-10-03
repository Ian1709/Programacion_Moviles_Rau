package com.tecsup.mibodega.ui.cliente.screens.carrito

import androidx.compose.runtime.Composable
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.Producto

/**
 * Delegado de compatibilidad para CarritoScreen.
 */
@Composable
fun CarritoScreen(
    carrito: List<ItemCarrito>,
    onVolver: () -> Unit,
    onIncrementar: (Producto) -> Unit,
    onDecrementar: (Producto) -> Unit,
    onEliminar: (Producto) -> Unit,
    onContinuarPedido: () -> Unit
) {
    PantallaCarrito(
        carrito = carrito,
        onVolver = onVolver,
        onIncrementar = onIncrementar,
        onDecrementar = onDecrementar,
        onEliminar = onEliminar,
        onContinuarPedido = onContinuarPedido
    )
}
