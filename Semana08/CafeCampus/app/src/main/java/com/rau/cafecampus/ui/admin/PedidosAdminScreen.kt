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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rau.cafecampus.ui.components.AdminBottomBar
import com.rau.cafecampus.ui.components.AdminTab
import com.rau.cafecampus.ui.components.EstadoChip
import com.rau.cafecampus.ui.theme.AzulAdmin
import com.rau.cafecampus.viewmodel.PedidosAdminViewModel
import com.rau.cafecampus.viewmodel.SesionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidosAdminScreen(
    pedidosAdminViewModel: PedidosAdminViewModel,
    sesionViewModel: SesionViewModel? = null,
    onVolver: () -> Unit,
    onGestionarMenu: () -> Unit,
    onAjustes: () -> Unit,
    onDetallePedido: (Long) -> Unit
) {
    val todosLosPedidos by pedidosAdminViewModel.todosLosPedidos.collectAsState()
    var tabSeleccionada by remember { mutableIntStateOf(0) } // 0: Nuevos (PENDIENTE), 1: Preparando (EN_PREPARACION), 2: Listos (LISTO)

    val estadoFiltro = when (tabSeleccionada) {
        0 -> "PENDIENTE"
        1 -> "EN_PREPARACION"
        2 -> "LISTO"
        else -> "PENDIENTE"
    }

    val pedidosFiltrados = todosLosPedidos.filter { it.pedido.estado.equals(estadoFiltro, ignoreCase = true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pedidos", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface) },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text("‹", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            AdminBottomBar(
                tabActual = AdminTab.PEDIDOS,
                onTabSeleccionada = { tab ->
                    when (tab) {
                        AdminTab.PANEL -> onVolver()
                        AdminTab.PEDIDOS -> {}
                        AdminTab.MENU -> onGestionarMenu()
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
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Segmented Control Tabs (Nuevos | Preparando | Listos)
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val pestañas = listOf("Nuevos", "Preparando", "Listos")
                    pestañas.forEachIndexed { index, titulo ->
                        val seleccionada = tabSeleccionada == index
                        Surface(
                            color = if (seleccionada) AzulAdmin else Color.Transparent,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .clickable { tabSeleccionada = index }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = titulo,
                                    color = if (seleccionada) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (pedidosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay pedidos en esta sección", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(pedidosFiltrados, key = { it.pedido.id }) { item ->
                        val pedido = item.pedido
                        val detalles = item.detalles

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onDetallePedido(pedido.id) },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("👤", fontSize = 20.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "%04d Ian Rau".format(pedido.id),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    EstadoChip(estado = pedido.estado)
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                val resumenItems = detalles.joinToString(" + ") { "${it.cantidad} x ${it.nombreProducto}" }
                                Text(
                                    text = resumenItems,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 13.sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Recojo ${pedido.horaRecojo} · S/ %.2f %s".format(
                                            pedido.total,
                                            if (pedido.nota.isNotBlank()) "· Nota: ${pedido.nota}" else ""
                                        ),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 12.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Botones de Acción Rápida estilo Maqueta
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    when (pedido.estado.uppercase()) {
                                        "PENDIENTE" -> {
                                            Button(
                                                onClick = { pedidosAdminViewModel.actualizarEstadoPedido(pedido.id, "EN_PREPARACION", sesionViewModel) },
                                                colors = ButtonDefaults.buttonColors(containerColor = AzulAdmin),
                                                shape = RoundedCornerShape(12.dp),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text("Pasar a Preparando", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                            }
                                            Button(
                                                onClick = { pedidosAdminViewModel.actualizarEstadoPedido(pedido.id, "CANCELADO", sesionViewModel) },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE), contentColor = Color(0xFFC62828)),
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Text("Cancelar", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                        "EN_PREPARACION" -> {
                                            Button(
                                                onClick = { pedidosAdminViewModel.actualizarEstadoPedido(pedido.id, "LISTO", sesionViewModel) },
                                                colors = ButtonDefaults.buttonColors(containerColor = AzulAdmin),
                                                shape = RoundedCornerShape(12.dp),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text("Marcar como Listo 🔔", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                        "LISTO" -> {
                                            Button(
                                                onClick = { pedidosAdminViewModel.actualizarEstadoPedido(pedido.id, "ENTREGADO", sesionViewModel) },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                                                shape = RoundedCornerShape(12.dp),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text("Entregar a Cliente 📦", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
