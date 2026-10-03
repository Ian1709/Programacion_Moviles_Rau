package com.tecsup.mibodega.ui.cliente.screens.inicio

import androidx.compose.runtime.Composable
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake

/**
 * Delegado de compatibilidad para InicioScreen.
 */
@Composable
fun InicioScreen(
    productos: List<Producto> = listaProductosFake,
    cantidadCarrito: Int,
    onVerCarrito: () -> Unit,
    onProductoClick: (Producto) -> Unit,
    onAgregarProducto: (Producto) -> Unit
) {
    PantallaInicio(
        productos = productos,
        cantidadCarrito = cantidadCarrito,
        onVerCarrito = onVerCarrito,
        onProductoClick = onProductoClick,
        onAgregarProducto = onAgregarProducto
    )
}
