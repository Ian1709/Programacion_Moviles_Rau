package com.tecsup.mibodega.ui.cliente.screens.inicio

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaCategorias
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.componentes.BarraNavegacionInferior
import com.tecsup.mibodega.ui.componentes.BarraSuperiorBodega
import com.tecsup.mibodega.ui.componentes.ProductoCard
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.VerdeBodega

enum class OpcionOrden {
    DEFECTO,
    MENOR_A_MAYOR,
    MAYOR_A_MENOR
}

/**
 * PantallaInicio (Fase 2 - Commit 3): Refinamiento visual y UI de la
 * barra de búsqueda en tiempo real y Empty State.
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

    val productosFiltrados = productos.filter { producto ->
        val coincideCategoria = categoriaSeleccionada == "Todos" || producto.categoria == categoriaSeleccionada
        val coincideBusqueda = textoBusqueda.isBlank() || 
                producto.nombre.contains(textoBusqueda, ignoreCase = true) ||
                producto.descripcion.contains(textoBusqueda, ignoreCase = true)
        val coincideFavoritos = !soloFavoritos || favoritosIds.contains(producto.id)
        
        coincideCategoria && coincideBusqueda && coincideFavoritos
    }

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
            // Buscador refactorizado visualmente
            item {
                OutlinedTextField(
                    value = textoBusqueda,
                    onValueChange = { textoBusqueda = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 4.dp),
                    placeholder = { 
                        Text(
                            text = "Buscar en ${if (categoriaSeleccionada == "Todos") "todo" else categoriaSeleccionada.lowercase()}...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    leadingIcon = { 
                        Icon(
                            imageVector = Icons.Default.Search, 
                            contentDescription = "Buscar",
                            tint = VerdeBodega
                        ) 
                    },
                    trailingIcon = {
                        if (textoBusqueda.isNotEmpty()) {
                            IconButton(onClick = { textoBusqueda = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear, 
                                    contentDescription = "Limpiar búsqueda",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp), // Bordes más redondeados
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = VerdeBodega,
                        cursorColor = VerdeBodega
                    )
                )
            }

            // LazyRow de Categorías
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

            // Filtros de ordenamiento y favoritos
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        OutlinedButton(
                            onClick = { menuOrdenExpandido = true },
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = when (ordenSeleccionado) {
                                    OpcionOrden.DEFECTO -> "Ordenar por precio"
                                    OpcionOrden.MENOR_A_MAYOR -> "Menor a Mayor"
                                    OpcionOrden.MAYOR_A_MENOR -> "Mayor a Menor"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Menú ordenamiento",
                                tint = MaterialTheme.colorScheme.onSurface
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
                                text = { Text("Precio: Menor a Mayor") },
                                onClick = {
                                    ordenSeleccionado = OpcionOrden.MENOR_A_MAYOR
                                    menuOrdenExpandido = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Precio: Mayor a Menor") },
                                onClick = {
                                    ordenSeleccionado = OpcionOrden.MAYOR_A_MENOR
                                    menuOrdenExpandido = false
                                }
                            )
                        }
                    }

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

            // Encabezado de Productos
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val encabezado = if (textoBusqueda.isNotEmpty()) {
                        "Resultados de búsqueda"
                    } else if (soloFavoritos) {
                        "Mis Favoritos"
                    } else {
                        "Categoría: $categoriaSeleccionada"
                    }
                    
                    Text(
                        text = encabezado,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = VerdeBodega
                    )
                    Text(
                        text = "${productosOrdenados.size} prod.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Grilla de Productos o Estado Vacío refinado
            if (productosOrdenados.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (textoBusqueda.isNotEmpty()) Icons.Default.SearchOff else Icons.Default.FavoriteBorder,
                                    contentDescription = "Sin resultados",
                                    tint = VerdeBodega,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                            
                            Spacer(Modifier.height(16.dp))
                            
                            val mensajeVacio = if (textoBusqueda.isNotEmpty()) {
                                "No se encontraron resultados para \"$textoBusqueda\" en la categoría seleccionada."
                            } else if (soloFavoritos) {
                                "Aún no tienes productos marcados como favoritos."
                            } else {
                                "No hay productos disponibles en esta categoría."
                            }
                            
                            Text(
                                text = mensajeVacio,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
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
    val fondo = if (seleccionado) VerdeBodega else MaterialTheme.colorScheme.surfaceVariant
    val contenido = if (seleccionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

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
