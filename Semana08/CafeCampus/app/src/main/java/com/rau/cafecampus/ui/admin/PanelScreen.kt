package com.rau.cafecampus.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rau.cafecampus.ui.components.AdminBottomBar
import com.rau.cafecampus.ui.components.AdminTab
import com.rau.cafecampus.ui.theme.AzulAdmin
import com.rau.cafecampus.viewmodel.PanelAdminViewModel
import com.rau.cafecampus.viewmodel.SesionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelScreen(
    panelViewModel: PanelAdminViewModel,
    sesionViewModel: SesionViewModel,
    onVerPedidos: () -> Unit,
    onGestionarMenu: () -> Unit,
    onAjustes: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val metricas by panelViewModel.metricas.collectAsState()
    val todosPedidos by panelViewModel.todosLosPedidos.collectAsState()
    val nuevosPedidos = todosPedidos.filter { it.pedido.estado.uppercase() == "PENDIENTE" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Panel de control",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Cafetería · Hoy",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                },
                actions = {
                    Text(
                        text = "🔔",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            AdminBottomBar(
                tabActual = AdminTab.PANEL,
                onTabSeleccionada = { tab ->
                    when (tab) {
                        AdminTab.PANEL -> {}
                        AdminTab.PEDIDOS -> onVerPedidos()
                        AdminTab.MENU -> onGestionarMenu()
                        AdminTab.AJUSTES -> onAjustes()
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 3 Tarjetas de Métricas de Colores (Nuevos, Preparando, Listos)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricBox(
                        numero = "${metricas.pendientesCount}",
                        label = "Nuevos",
                        colorFondo = Color(0xFFE8F0FE),
                        colorNumero = Color(0xFF1976D2),
                        modifier = Modifier.weight(1f)
                    )
                    MetricBox(
                        numero = "${metricas.enPreparacionCount}",
                        label = "Preparando",
                        colorFondo = Color(0xFFFFF3E0),
                        colorNumero = Color(0xFFE65100),
                        modifier = Modifier.weight(1f)
                    )
                    MetricBox(
                        numero = "${metricas.listosCount}",
                        label = "Listos",
                        colorFondo = Color(0xFFE8F5E9),
                        colorNumero = Color(0xFF2E7D32),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Tarjeta Morada Grande: Ventas del día
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = AzulAdmin)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Ventas del día",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "S/ %.2f".format(metricas.ventasTotales),
                                color = Color.White,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "${metricas.totalPedidos} pedidos",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Sección: Pedidos por hora (Simulación visual de gráfico de barras)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Pedidos por hora",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            val horas = listOf("8h" to 3, "9h" to 6, "10h" to 9, "11h" to 5, "12h" to 7)
                            val maxVal = 10

                            horas.forEach { (hora, cant) ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "$cant", fontSize = 10.sp, color = Color.Gray)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .width(28.dp)
                                            .height(((cant.toFloat() / maxVal) * 50).dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (hora == "10h") AzulAdmin else Color(0xFFC5CAE9))
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(text = hora, fontSize = 11.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }

            // Sección: Nuevos pedidos (Ver todos)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nuevos pedidos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    TextButton(onClick = onVerPedidos) {
                        Text(text = "Ver todos", color = AzulAdmin, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (nuevosPedidos.isEmpty()) {
                item {
                    Text(
                        text = "No hay pedidos pendientes por preparar",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }
            } else {
                items(nuevosPedidos.take(3), key = { it.pedido.id }) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "#%04d Ian R.".format(item.pedido.id),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                val resumen = item.detalles.joinToString(" · ") { "${it.cantidad}x ${it.nombreProducto}" }
                                Text(
                                    text = "$resumen · ${item.pedido.horaRecojo}",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }
                            Button(
                                onClick = onVerPedidos,
                                colors = ButtonDefaults.buttonColors(containerColor = AzulAdmin),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Preparar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricBox(
    numero: String,
    label: String,
    colorFondo: Color,
    colorNumero: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = colorFondo,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.height(90.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = numero,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorNumero
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                color = colorNumero,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
