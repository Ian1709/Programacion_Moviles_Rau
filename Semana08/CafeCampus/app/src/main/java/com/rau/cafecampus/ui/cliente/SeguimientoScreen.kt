package com.rau.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.rau.cafecampus.ui.components.StepperEstado
import com.rau.cafecampus.ui.theme.FondoSegmentoGris
import com.rau.cafecampus.ui.theme.NaranjaEstudiante
import com.rau.cafecampus.viewmodel.PedidosClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeguimientoScreen(
    pedidoId: Long,
    pedidosViewModel: PedidosClienteViewModel,
    onVolverAlMenu: () -> Unit,
    onVerMisPedidos: () -> Unit
) {
    val misPedidos by pedidosViewModel.misPedidos.collectAsState()
    val pedidoConDetalle = misPedidos.find { it.pedido.id == pedidoId } ?: misPedidos.firstOrNull()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Seguimiento",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onVolverAlMenu) {
                        Text("‹", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        if (pedidoConDetalle == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Cargando información del pedido...")
            }
        } else {
            val pedido = pedidoConDetalle.pedido
            val detalles = pedidoConDetalle.detalles

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    // Tarjeta Naranja de Cabecera
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = NaranjaEstudiante)
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
                                    text = "Pedido #%04d".format(pedido.id),
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Recojo a las ${pedido.horaRecojo}",
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Tiempo estimado: 10 min",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 12.sp
                                )
                            }
                            Text(
                                text = "🕒",
                                fontSize = 36.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Linea de Progreso Vertical Stepper
                    StepperEstado(estadoActual = pedido.estado)
                }

                // Tarjeta Resumen Inferior
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = FondoSegmentoGris.copy(alpha = 0.6f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val resumenItems = detalles.joinToString(" · ") { "${it.cantidad} x ${it.nombreProducto}" }
                        Text(
                            text = resumenItems,
                            fontSize = 13.sp,
                            color = Color.Gray,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total S/ %.2f".format(pedido.total),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = NaranjaEstudiante
                            )
                            TextButton(onClick = onVerMisPedidos) {
                                Text("Ver historial ›", color = NaranjaEstudiante, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
