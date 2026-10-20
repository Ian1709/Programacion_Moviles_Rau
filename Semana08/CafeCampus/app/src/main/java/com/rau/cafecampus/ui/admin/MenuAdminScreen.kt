package com.rau.cafecampus.ui.admin

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rau.cafecampus.ui.components.AdminBottomBar
import com.rau.cafecampus.ui.components.AdminTab
import com.rau.cafecampus.ui.components.ProductoIcono
import com.rau.cafecampus.ui.theme.AzulAdmin
import com.rau.cafecampus.ui.theme.FondoSegmentoGris
import com.rau.cafecampus.viewmodel.MenuAdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuAdminScreen(
    menuAdminViewModel: MenuAdminViewModel,
    onVolver: () -> Unit,
    onVerPedidos: () -> Unit,
    onAjustes: () -> Unit,
    onCrearProducto: () -> Unit,
    onEditarProducto: (Int) -> Unit
) {
    val productosFiltrados by menuAdminViewModel.productosFiltrados.collectAsState()
    val categoriaFiltro by menuAdminViewModel.categoriaFiltro.collectAsState()

    val categorias = listOf("TODOS", "BEBIDAS", "COMIDAS", "POSTRES")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión del Menú", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text("‹", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCrearProducto,
                containerColor = AzulAdmin,
                contentColor = Color.White
            ) {
                Text("+ Nuevo", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp))
            }
        },
        bottomBar = {
            AdminBottomBar(
                tabActual = AdminTab.MENU,
                onTabSeleccionada = { tab ->
                    when (tab) {
                        AdminTab.PANEL -> onVolver()
                        AdminTab.PEDIDOS -> onVerPedidos()
                        AdminTab.MENU -> {}
                        AdminTab.AJUSTES -> onAjustes()
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Filtros de categoría
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categorias) { cat ->
                    val seleccionado = categoriaFiltro.equals(cat, ignoreCase = true)
                    FilterChip(
                        selected = seleccionado,
                        onClick = { menuAdminViewModel.seleccionarCategoriaFiltro(cat) },
                        label = { Text(cat) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AzulAdmin,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (productosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay productos en esta categoría", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(productosFiltrados, key = { it.id }) { producto ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onEditarProducto(producto.id) },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = FondoSegmentoGris.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ProductoIcono(categoria = producto.categoria, tamano = 52.dp)

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = producto.nombre,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = producto.descripcion,
                                        color = Color.Gray,
                                        fontSize = 12.sp,
                                        maxLines = 1
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "S/ %.2f".format(producto.precio),
                                        fontWeight = FontWeight.Bold,
                                        color = AzulAdmin,
                                        fontSize = 14.sp
                                    )
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = if (producto.disponible) "Disponible" else "Agotado",
                                        fontSize = 11.sp,
                                        color = if (producto.disponible) Color(0xFF2E7D32) else Color.Red,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Switch(
                                        checked = producto.disponible,
                                        onCheckedChange = { menuAdminViewModel.toggleDisponibilidad(producto) },
                                        colors = SwitchDefaults.colors(checkedThumbColor = AzulAdmin)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
