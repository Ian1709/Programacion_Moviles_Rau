package com.tecsup.mibodega.ui.cliente.screens.carrito

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.SelectorCantidad
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

enum class TipoEntrega {
    RECOJO_TIENDA,
    DELIVERY
}

private const val COSTO_ENVIO_DELIVERY = 5.00
private const val COSTO_ENVIO_RECOJO = 0.00

/**
 * PantallaCarrito (Hito 7):
 * - Vista "Carrito vacío"
 * - AlertDialog de confirmación para eliminar productos
 * - Selección Recojo en Tienda (S/ 0.00) vs. Delivery (S/ 5.00) mediante RadioButtons
 * - Cálculo reactivo del subtotal, costo de envío y total
 */
@Composable
fun PantallaCarrito(
    carrito: List<ItemCarrito>,
    onVolver: () -> Unit,
    onIncrementar: (Producto) -> Unit,
    onDecrementar: (Producto) -> Unit,
    onEliminar: (Producto) -> Unit,
    onContinuarPedido: () -> Unit
) {
    var productoAEliminar by remember { mutableStateOf<Producto?>(null) }
    var tipoEntrega by remember { mutableStateOf(TipoEntrega.DELIVERY) }

    val subtotal = carrito.sumOf { it.producto.precio * it.cantidad }
    val costoEnvio = if (tipoEntrega == TipoEntrega.DELIVERY) COSTO_ENVIO_DELIVERY else COSTO_ENVIO_RECOJO
    val total = subtotal + costoEnvio

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        EncabezadoCarrito(onVolver = onVolver)

        if (carrito.isEmpty()) {
            VistaCarritoVacio(onIrAlCatalogo = onVolver)
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(carrito, key = { it.producto.id }) { item ->
                    FilaCarrito(
                        item = item,
                        onIncrementar = { onIncrementar(item.producto) },
                        onDecrementar = {
                            if (item.cantidad == 1) {
                                productoAEliminar = item.producto
                            } else {
                                onDecrementar(item.producto)
                            }
                        },
                        onSolicitarEliminar = { productoAEliminar = item.producto }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }

                // Sección Método de Entrega (RadioButtons)
                item {
                    Spacer(Modifier.height(12.dp))
                    SeccionTipoEntrega(
                        tipoEntrega = tipoEntrega,
                        onTipoEntregaCambia = { tipoEntrega = it }
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }

            ResumenYBoton(
                subtotal = subtotal,
                costoEnvio = costoEnvio,
                total = total,
                tipoEntrega = tipoEntrega,
                onContinuarPedido = onContinuarPedido
            )
        }
    }

    // Modal de confirmación para eliminar producto
    if (productoAEliminar != null) {
        AlertDialog(
            onDismissRequest = { productoAEliminar = null },
            title = {
                Text(
                    text = "Eliminar producto",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas quitar \"${productoAEliminar?.nombre}\" del carrito de compras?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        productoAEliminar?.let { onEliminar(it) }
                        productoAEliminar = null
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { productoAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun VistaCarritoVacio(onIrAlCatalogo: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .background(GrisClaro, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.RemoveShoppingCart,
                    contentDescription = "Carrito vacío",
                    tint = VerdeBodega,
                    modifier = Modifier.size(56.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Tu carrito está vacío 🛒",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Parece que aún no has agregado productos. Explora el catálogo y llena tu bodega con los mejores precios.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))

            BotonPrimario(
                texto = "Explorar catálogo",
                onClick = onIrAlCatalogo
            )
        }
    }
}

@Composable
private fun EncabezadoCarrito(onVolver: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onVolver) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver"
            )
        }
        Text(
            text = "Mi carrito",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun FilaCarrito(
    item: ItemCarrito,
    onIncrementar: () -> Unit,
    onDecrementar: () -> Unit,
    onSolicitarEliminar: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(GrisClaro, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingBasket,
                contentDescription = item.producto.nombre,
                tint = VerdeBodega,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.producto.nombre,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "S/ %.2f c/u".format(item.producto.precio),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Subtotal: S/ %.2f".format(item.producto.precio * item.cantidad),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = VerdeBodega
            )
        }

        SelectorCantidad(
            cantidad = item.cantidad,
            onIncrementar = onIncrementar,
            onDecrementar = onDecrementar
        )

        IconButton(onClick = onSolicitarEliminar) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Eliminar ${item.producto.nombre}",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun SeccionTipoEntrega(
    tipoEntrega: TipoEntrega,
    onTipoEntregaCambia: (TipoEntrega) -> Unit
) {
    Column {
        Text(
            text = "Método de entrega",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTipoEntregaCambia(TipoEntrega.DELIVERY) }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = tipoEntrega == TipoEntrega.DELIVERY,
                        onClick = { onTipoEntregaCambia(TipoEntrega.DELIVERY) },
                        colors = RadioButtonDefaults.colors(selectedColor = VerdeBodega)
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = null,
                        tint = VerdeBodega,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Delivery a domicilio",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Recibe tu pedido en la puerta de tu casa",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "S/ %.2f".format(COSTO_ENVIO_DELIVERY),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = VerdeBodega
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTipoEntregaCambia(TipoEntrega.RECOJO_TIENDA) }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = tipoEntrega == TipoEntrega.RECOJO_TIENDA,
                        onClick = { onTipoEntregaCambia(TipoEntrega.RECOJO_TIENDA) },
                        colors = RadioButtonDefaults.colors(selectedColor = VerdeBodega)
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Store,
                        contentDescription = null,
                        tint = VerdeBodega,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Recojo en tienda",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Retira gratis en nuestra bodega principal",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "Gratis",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = VerdeBodega
                    )
                }
            }
        }
    }
}

@Composable
private fun ResumenYBoton(
    subtotal: Double,
    costoEnvio: Double,
    total: Double,
    tipoEntrega: TipoEntrega,
    onContinuarPedido: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
        FilaResumen(etiqueta = "Subtotal productos", valor = subtotal)
        FilaResumen(
            etiqueta = if (tipoEntrega == TipoEntrega.DELIVERY) "Costo de delivery" else "Costo de recojo",
            valor = costoEnvio
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total a pagar",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "S/ %.2f".format(total),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = VerdeBodega
            )
        }

        Spacer(Modifier.height(16.dp))

        BotonPrimario(
            texto = "Continuar pedido (S/ %.2f)".format(total),
            onClick = onContinuarPedido
        )
    }
}

@Composable
private fun FilaResumen(etiqueta: String, valor: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = etiqueta, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = if (valor == 0.0) "S/ 0.00 (Gratis)" else "S/ %.2f".format(valor),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PantallaCarritoPreview() {
    val carritoEjemplo = listOf(
        ItemCarrito(listaProductosFake[0], 2),
        ItemCarrito(listaProductosFake[1], 1)
    )
    BodegaTheme {
        PantallaCarrito(
            carrito = carritoEjemplo,
            onVolver = {},
            onIncrementar = {},
            onDecrementar = {},
            onEliminar = {},
            onContinuarPedido = {}
        )
    }
}
