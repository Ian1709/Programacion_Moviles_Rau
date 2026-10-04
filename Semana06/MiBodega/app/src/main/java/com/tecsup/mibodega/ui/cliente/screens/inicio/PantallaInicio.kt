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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
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
 * PantallaInicio (Fase 2 - Commit 2): Lógica combinada de filtros
 * (Buscador + LazyRow de categorías) y actualización del grid/listado.
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
    // 1. ESTADO DEL FILTRO POR CATEGORÍA
    var categoriaSeleccionada by remember(categoriaInicial) { mutableStateOf(categoriaInicial) }
    
    // 2. ESTADO MUTABLE DEL BUSCADOR EN TIEMPO REAL
    var textoBusqueda by remember { mutableStateOf("") }
    
    var ordenSeleccionado by remember { mutableStateOf(OpcionOrden.DEFECTO) }
    var soloFavoritos by remember { mutableStateOf(false) }
    var menuOrdenExpandido by remember { mutableStateOf(false) }

    // =========================================================================
    // LÓGICA COMBINADA DE FILTROS: Buscador + Categoría + Favoritos
    // =========================================================================
    val productosFiltrados = productos.filter { producto ->
        
        // CONDICIÓN A: Filtro por categoría seleccionada
        val coincideCategoria = categoriaSeleccionada == "Todos" || producto.categoria == categoriaSeleccionada
        
        // CONDICIÓN B: Filtro dinámico por el texto del buscador
        // Asegura que al borrar el texto, vuelva a mostrar los de la categoría
        val coincideBusqueda = textoBusqueda.isBlank() || 
                producto.nombre.contains(textoBusqueda, ignoreCase = true) ||
                producto.descripcion.contains(textoBusqueda, ignoreCase = true)
                
        // CONDICIÓN C: Filtro de estado favorito
        val coincideFavoritos = !soloFavoritos || favoritosIds.contains(producto.id)
        
        // OPERACIÓN AND (&&): Todas las condiciones deben cumplirse simultáneamente
        coincideCategoria && coincideBusqueda && coincideFavoritos
    }

    // Ordenamiento por precio (se aplica a la lista ya filtrada por categoría y buscador)
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
            // =================================================================
            // 1. BARRA DE BÚSQUEDA EN TIEMPO REAL
            // =================================================================
            item {
                OutlinedTextField(
                    value = textoBusqueda,
                    onValueChange = { nuevoTexto -> 
                        // Al escribir, actualiza el estado y dispara la recomposición de los filtros
                        textoBusqueda = nuevoTexto 
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    placeholder = { Text("Buscar en ${if (categoriaSeleccionada == "Todos") "todas las categorías" else categoriaSeleccionada}...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                    trailingIcon = {
                        // Botón para limpiar rápidamente el buscador sin alterar la categoría
                        if (textoBusqueda.isNotEmpty()) {
                            IconButton(onClick = { textoBusqueda = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Limpiar búsqueda")
                            }
                        }
                    },
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

            // =================================================================
            // 2. LAZYROW DE CATEGORÍAS FILTRABLES
            // =================================================================
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
                        // El contador de productos por categoría no se ve afectado por el buscador
                        val cantidadPorCategoria = if (categoria == "Todos") {
                            productos.size
                        } else {
                            productos.count { it.categoria == categoria }
                        }

                        ChipCategoria(
                            texto = "$categoria ($cantidadPorCategoria)",
                            seleccionado = categoria == categoriaSeleccionada && !soloFavoritos,
                            onClick = {
                                // Al cambiar la categoría, el buscador en tiempo real se mantiene activo
                                // aplicando la COMBINACIÓN DE AMBOS FILTROS.
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

            // =================================================================
            // 3. ACTUALIZACIÓN DEL GRID Y LISTADO
            // =================================================================
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val encabezado = if (textoBusqueda.isNotEmpty()) {
                        "Resultados para \"$textoBusqueda\""
                    } else if (soloFavoritos) {
                        "Mis Productos Favoritos"
                    } else {
                        "Categoría: $categoriaSeleccionada"
                    }
                    
                    Text(
                        text = encabezado,
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
                        val mensajeVacio = if (textoBusqueda.isNotEmpty()) {
                            "No se encontraron resultados para \"$textoBusqueda\" en esta categoría."
                        } else if (soloFavoritos) {
                            "No tienes productos marcados como favoritos"
                        } else {
                            "No hay productos en esta categoría"
                        }
                        
                        Text(
                            text = mensajeVacio,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
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
