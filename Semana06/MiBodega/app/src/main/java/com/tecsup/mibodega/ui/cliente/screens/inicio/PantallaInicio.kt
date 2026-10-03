package com.tecsup.mibodega.ui.cliente.screens.inicio

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaCategorias
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.componentes.BarraNavegacionInferior
import com.tecsup.mibodega.ui.componentes.BarraSuperiorBodega
import com.tecsup.mibodega.ui.componentes.ProductoCard
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

enum class OpcionOrden {
    DEFECTO,
    MENOR_A_MAYOR,
    MAYOR_A_MENOR
}

/**
 * PantallaInicio (Hito 5): Catálogo con LazyRow de categorías filtrables dinámicamente.
 */
@Composable
fun PantallaInicio(
    productos: List<Producto> = listaProductosFake,
    cantidadCarrito: Int,
    favoritosIds: Set<Int> = emptySet(),
    categoriaInicial: String = "Todos",
    onToggleFavorito: (Int) -> Unit = {},
    onVerCarrito: () -> Unit,
    onProductoClick: (Producto) -> Unit,
    onAgregarProducto: (Producto) -> Unit,
    destinoSeleccionado: Int = 0,
    onSeleccionarDestino: (Int) -> Unit = {}
) {
    var categoriaSeleccionada by remember(categoriaInicial) { mutableStateOf(categoriaInicial) }
    var textoBusqueda by remember { mutableStateOf("") }
    var ordenSeleccionado by remember { mutableStateOf(OpcionOrden.DEFECTO) }
    var soloFavoritos by remember { mutableStateOf(false) }
    var menuOrdenExpandido by remember { mutableStateOf(false) }

    // Filtrado dinámico por categoría, búsqueda y favoritos
    val productosFiltrados = productos.filter { producto ->
        val coincideCategoria = categoriaSeleccionada == "Todos" || producto.categoria == categoriaSeleccionada
        val coincideBusqueda = producto.nombre.contains(textoBusqueda, ignoreCase = true) ||
                producto.descripcion.contains(textoBusqueda, ignoreCase = true)
        val coincideFavoritos = !soloFavoritos || favoritosIds.contains(producto.id)
        coincideCategoria && coincideBusqueda && coincideFavoritos
    }

    // Ordenamiento por precio
    val productosOrdenados = when (ordenSeleccionado) {
        OpcionOrden.MENOR_A_MAYOR -> productosFiltrados.sortedBy { it.precio }
        OpcionOrden.MAYOR_A_MENOR -> productosFiltrados.sortedByDescending { it.precio }
        OpcionOrden.DEFECTO -> productosFiltrados
    }

    Scaffold(
        topBar = {
            BarraSuperiorBodega(
                titulo = "Mi Bodega",
                cantidadCarrito = cantidadCarrito,
                onVerCarrito = onVerCarrito
            )
        },
        bottomBar = {
            BarraNavegacionInferior(
                destinoSeleccionado = destinoSeleccionado,
                onSeleccionarDestino = onSeleccionarDestino
            )
        }
    ) { paddingInterno ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Buscador
            item {
                OutlinedTextField(
                    value = textoBusqueda,
                    onValueChange = { textoBusqueda = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    placeholder = { Text("Buscar productos...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = GrisClaro,
                        focusedContainerColor = GrisClaro,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = VerdeBodega
                    )
                )
            }

            // LazyRow de Categorías Filtrables
            item {
                Text(
                    text = "Categorías",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 6.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(listaCategorias) { categoria ->
                        val cantidadPorCategoria = if (categoria == "Todos") {
                            productos.size
                        } else {
                            productos.count { it.categoria == categoria }
                        }

                        ChipCategoria(
                            texto = "$categoria ($cantidadPorCategoria)",
                            seleccionado = categoria == categoriaSeleccionada && !soloFavoritos,
                            onClick = {
                                categoriaSeleccionada = categoria
                                soloFavoritos = false
                            }
                        )
                    }
                }
            }

            // Opciones de Ordenamiento y Filtro de Favoritos
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón con menú desplegable para ordenar por precio
                    Box {
                        OutlinedButton(
                            onClick = { menuOrdenExpandido = true },
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = when (ordenSeleccionado) {
                                    OpcionOrden.DEFECTO -> "Ordenar por precio"
                                    OpcionOrden.MENOR_A_MAYOR -> "Precio: Menor a Mayor"
                                    OpcionOrden.MAYOR_A_MENOR -> "Precio: Mayor a Menor"
                                },
                                style = MaterialTheme.typography.bodySmall
                            )
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Menú ordenamiento"
                            )
                        }

                        DropdownMenu(
                            expanded = menuOrdenExpandido,
                            onDismissRequest = { menuOrdenExpandido = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Por defecto") },
                                onClick = {
                                    ordenSeleccionado = OpcionOrden.DEFECTO
                                    menuOrdenExpandido = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Precio: Menor a Mayor ⬆️") },
                                onClick = {
                                    ordenSeleccionado = OpcionOrden.MENOR_A_MAYOR
                                    menuOrdenExpandido = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Precio: Mayor a Menor ⬇️") },
                                onClick = {
                                    ordenSeleccionado = OpcionOrden.MAYOR_A_MENOR
                                    menuOrdenExpandido = false
                                }
                            )
                        }
                    }

                    // Chip para filtrar solo Favoritos
                    FilterChip(
                        selected = soloFavoritos,
                        onClick = { soloFavoritos = !soloFavoritos },
                        label = {
                            Text(
                                text = "Favoritos (${favoritosIds.size})",
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = if (soloFavoritos) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favoritos",
                                tint = if (soloFavoritos) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color.Red.copy(alpha = 0.1f),
                            selectedLabelColor = Color.Red
                        )
                    )
                }
            }

            // Encabezado de Productos con categoría activa
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (soloFavoritos) "Mis Productos Favoritos" else "Categoría: $categoriaSeleccionada",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${productosOrdenados.size} prod.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Grilla de Productos Renderizada en filas dentro de la LazyColumn
            if (productosOrdenados.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (soloFavoritos) "No tienes productos marcados como favoritos" else "No hay productos en esta categoría",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                val parejasProductos = productosOrdenados.chunked(2)
                items(parejasProductos) { pareja ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val p1 = pareja[0]
                        ProductoCard(
                            producto = p1,
                            onClick = { onProductoClick(p1) },
                            onAgregar = { onAgregarProducto(p1) },
                            esFavorito = favoritosIds.contains(p1.id),
                            onToggleFavorito = { onToggleFavorito(p1.id) },
                            modifier = Modifier.weight(1f)
                        )

                        if (pareja.size > 1) {
                            val p2 = pareja[1]
                            ProductoCard(
                                producto = p2,
                                onClick = { onProductoClick(p2) },
                                onAgregar = { onAgregarProducto(p2) },
                                esFavorito = favoritosIds.contains(p2.id),
                                onToggleFavorito = { onToggleFavorito(p2.id) },
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChipCategoria(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    val fondo = if (seleccionado) VerdeBodega else GrisClaro
    val contenido = if (seleccionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier
            .background(fondo, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = texto, color = contenido, fontWeight = FontWeight.Medium)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PantallaInicioPreview() {
    BodegaTheme {
        PantallaInicio(
            cantidadCarrito = 2,
            favoritosIds = setOf(1, 3),
            onVerCarrito = {},
            onProductoClick = {},
            onAgregarProducto = {}
        )
    }
}
