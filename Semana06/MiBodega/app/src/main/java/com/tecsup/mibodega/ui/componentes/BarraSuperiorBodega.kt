package com.tecsup.mibodega.ui.componentes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

/**
 * TopBar reutilizable con Badge reactivo en el ícono del carrito de compras.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperiorBodega(
    titulo: String = "Mi Bodega",
    cantidadCarrito: Int = 0,
    onVerCarrito: () -> Unit = {},
    mostrarVolver: Boolean = false,
    onVolver: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(titulo, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            if (mostrarVolver && onVolver != null) {
                IconButton(onClick = onVolver) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onVerCarrito) {
                BadgedBox(
                    badge = {
                        if (cantidadCarrito > 0) {
                            Badge {
                                Text(if (cantidadCarrito > 99) "99+" else "$cantidadCarrito")
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Ver carrito de compras"
                    )
                }
            }
        }
    )
}
