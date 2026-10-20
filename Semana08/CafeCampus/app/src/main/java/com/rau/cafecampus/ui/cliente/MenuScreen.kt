package com.rau.cafecampus.ui.cliente

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rau.cafecampus.ui.components.CafeBottomBar
import com.rau.cafecampus.ui.components.CafeTextField
import com.rau.cafecampus.ui.components.ClienteTab
import com.rau.cafecampus.ui.components.ProductoCard
import com.rau.cafecampus.ui.theme.NaranjaEstudiante
import com.rau.cafecampus.viewmodel.MenuViewModel
import com.rau.cafecampus.viewmodel.SesionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    menuViewModel: MenuViewModel,
    sesionViewModel: SesionViewModel,
    onProductoClick: (Int) -> Unit,
    onIrAConfirmarPedido: () -> Unit,
    onIrAPedidos: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val uiState by menuViewModel.uiState.collectAsState()
    val productosFiltrados by menuViewModel.productosFiltrados.collectAsState()
    val textoBusqueda by menuViewModel.textoBusqueda.collectAsState()
    val nombreUsuario by sesionViewModel.userName.collectAsState(initial = "")
    val snackbarHostState = remember { SnackbarHostState() }

    val categorias = listOf("TODOS", "BEBIDAS", "COMIDAS", "POSTRES")

    LaunchedEffect(uiState.mensajeInfo) {
        uiState.mensajeInfo?.let { mensaje ->
            snackbarHostState.showSnackbar(mensaje)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        val nombreAMostrar = if (nombreUsuario.isBlank() || nombreUsuario.contains("Juan", ignoreCase = true)) "Ian Rau" else nombreUsuario
                        Text(
                            text = "Hola, $nombreAMostrar 👋",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "¿Qué te gustaría pedir hoy?",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    TextButton(onClick = {
                        sesionViewModel.cerrarSesion()
                        onCerrarSesion()
                    }) {
                        Text("Salir", color = NaranjaEstudiante, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Column {
                if (uiState.carrito.isNotEmpty()) {
                    Surface(
                        shadowElevation = 8.dp,
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${menuViewModel.totalItemsCarrito()} productos en carrito",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Total: S/ %.2f".format(menuViewModel.totalCarrito()),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = NaranjaEstudiante
                                )
                            }

                            Button(
                                onClick = onIrAConfirmarPedido,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NaranjaEstudiante)
                            ) {
                                Text(
                                    text = "Ver Carrito",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
                CafeBottomBar(
                    tabActual = ClienteTab.MENU,
                    onTabSeleccionada = { tab ->
                        when (tab) {
                            ClienteTab.MENU -> {}
                            ClienteTab.PEDIDOS -> onIrAPedidos()
                            ClienteTab.PERFIL -> {
                                sesionViewModel.cerrarSesion()
                                onCerrarSesion()
                            }
                        }
                    }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Barra de Búsqueda
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                CafeTextField(
                    value = textoBusqueda,
                    onValueChange = { menuViewModel.onTextoBusquedaChange(it) },
                    label = "Buscar productos...",
                    activeColor = NaranjaEstudiante
                )
            }

            // Fila de Filtros de Categoría
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categorias) { cat ->
                    val seleccionada = uiState.categoriaSeleccionada.equals(cat, ignoreCase = true)
                    FilterChip(
                        selected = seleccionada,
                        onClick = { menuViewModel.seleccionarCategoria(cat) },
                        label = { Text(cat) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NaranjaEstudiante,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Lista de productos
            if (productosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se encontraron productos disponibles",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(productosFiltrados, key = { it.id }) { producto ->
                        ProductoCard(
                            producto = producto,
                            onClick = { onProductoClick(producto.id) },
                            onAgregarCarrito = { menuViewModel.agregarAlCarrito(producto, 1) }
                        )
                    }
                }
            }
        }
    }
}
