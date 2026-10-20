package com.rau.cafecampus.ui.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rau.cafecampus.ui.components.EstadoChip
import com.rau.cafecampus.ui.theme.AzulAdmin
import com.rau.cafecampus.ui.theme.FondoSegmentoGris
import com.rau.cafecampus.viewmodel.PedidosAdminViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallePedidoAdminScreen(
    pedidoId: Long,
    pedidosAdminViewModel: PedidosAdminViewModel,
    onVolver: () -> Unit
) {
    val todosPedidos by pedidosAdminViewModel.todosLosPedidos.collectAsState()
    val itemPedido = todosPedidos.find { it.pedido.id == pedidoId } ?: todosPedidos.firstOrNull()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del pedido", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text("‹", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        if (itemPedido == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Cargando detalle...")
            }
        } else {
            val pedido = itemPedido.pedido
            val detalles = itemPedido.detalles

            val formatoFecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val fechaFormateada = formatoFecha.format(Date(pedido.fecha))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Cabecera del Pedido
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = FondoSegmentoGris.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Pedido #%04d".format(pedido.id),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                EstadoChip(estado = pedido.estado)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Hoy · Recojo ${pedido.horaRecojo} ($fechaFormateada)",
                                color = Color.Gray,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                // Tarjeta de Datos del Cliente
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = "Cliente", fontSize = 12.sp, color = Color.Gray)
                            Text(
                                text = "Ian Rau · Estudiante",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }

                // Lista de Productos e Importe
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = FondoSegmentoGris.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            detalles.forEach { det ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "${det.nombreProducto}", fontWeight = FontWeight.Medium)
                                    Text(text = "x${det.cantidad}", color = Color.Gray)
                                    Text(
                                        text = "S/ %.2f".format(det.precioUnitario * det.cantidad),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Total", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(
                                    text = "S/ %.2f".format(pedido.total),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = AzulAdmin
                                )
                            }
                        }
                    }
                }

                // Nota del Cliente si existe
                if (pedido.nota.isNotBlank()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(text = "Nota del cliente", color = Color(0xFFF57F17), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = pedido.nota, color = Color(0xFF333333), fontSize = 14.sp)
                            }
                        }
                    }
                }

                // Selector de Estado
                item {
                    Column {
                        Text(
                            text = "Cambiar estado",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        val estados = listOf("PENDIENTE" to "Recibido", "EN_PREPARACION" to "Preparando", "LISTO" to "Listo", "ENTREGADO" to "Entregado")

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            estados.forEach { (codigo, etiqueta) ->
                                val seleccionado = pedido.estado.equals(codigo, ignoreCase = true)
                                Surface(
                                    color = if (seleccionado) AzulAdmin else FondoSegmentoGris,
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(38.dp)
                                        .clickable { pedidosAdminViewModel.actualizarEstadoPedido(pedido.id, codigo) }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = etiqueta,
                                            color = if (seleccionado) Color.White else Color.Black,
                                            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Botón Acción Principal y Cancelación
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        when (pedido.estado.uppercase()) {
                            "PENDIENTE" -> {
                                Button(
                                    onClick = { pedidosAdminViewModel.actualizarEstadoPedido(pedido.id, "EN_PREPARACION") },
                                    colors = ButtonDefaults.buttonColors(containerColor = AzulAdmin),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                ) {
                                    Text("Pasar a Preparando", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            "EN_PREPARACION" -> {
                                Button(
                                    onClick = { pedidosAdminViewModel.actualizarEstadoPedido(pedido.id, "LISTO") },
                                    colors = ButtonDefaults.buttonColors(containerColor = AzulAdmin),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                ) {
                                    Text("Marcar como Listo", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            "LISTO" -> {
                                Button(
                                    onClick = { pedidosAdminViewModel.actualizarEstadoPedido(pedido.id, "ENTREGADO") },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                ) {
                                    Text("Marcar como Entregado", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        if (pedido.estado.uppercase() != "CANCELADO" && pedido.estado.uppercase() != "ENTREGADO") {
                            OutlinedButton(
                                onClick = { pedidosAdminViewModel.actualizarEstadoPedido(pedido.id, "CANCELADO") },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.Red),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                Text("Cancelar pedido", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
