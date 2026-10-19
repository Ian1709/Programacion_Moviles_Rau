package com.rau.cafecampus.ui.cliente

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.rau.cafecampus.ui.components.ProductoIcono
import com.rau.cafecampus.ui.theme.FondoSegmentoGris
import com.rau.cafecampus.ui.theme.NaranjaEstudiante
import com.rau.cafecampus.viewmodel.MenuViewModel
import com.rau.cafecampus.viewmodel.PedidosClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmarPedidoScreen(
    menuViewModel: MenuViewModel,
    pedidosViewModel: PedidosClienteViewModel,
    onVolverAlMenu: () -> Unit,
    onPedidoConfirmado: (Long) -> Unit
) {
    val menuState by menuViewModel.uiState.collectAsState()
    val confirmarState by pedidosViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val horasRecojo = listOf("Ahora", "10:30", "11:00", "11:30")
    val formasPago = listOf("Pagar en caja", "Yape / Plin")

    val subtotal = menuViewModel.totalCarrito()

    LaunchedEffect(confirmarState.mensajeError) {
        confirmarState.mensajeError?.let { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Confirmar pedido",
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
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Card de Lista de Productos seleccionados
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = FondoSegmentoGris.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (menuState.carrito.isEmpty()) {
                            Text("El carrito está vacío", color = Color.Gray)
                        } else {
                            menuState.carrito.forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        ProductoIcono(categoria = item.producto.categoria, tamano = 44.dp)
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = item.producto.nombre,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp
                                            )
                                            Text(
                                                text = "x${item.cantidad}",
                                                color = Color.Gray,
                                                fontSize = 13.sp
                                            )
                                        }
                                    }
                                    Text(
                                        text = "S/ %.2f".format(item.subtotal),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Sección: Hora de recojo
            item {
                Column {
                    Text(
                        text = "Hora de recojo",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        horasRecojo.forEach { hora ->
                            val selected = confirmarState.horaRecojo == hora
                            Surface(
                                color = if (selected) NaranjaEstudiante else FondoSegmentoGris,
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp)
                                    .clickable { pedidosViewModel.onHoraRecojoChange(hora) }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = hora,
                                        color = if (selected) Color.White else Color.Black,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Sección: Forma de pago
            item {
                Column {
                    Text(
                        text = "Forma de pago",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        formasPago.forEach { pago ->
                            val selected = confirmarState.pago == pago
                            Surface(
                                color = if (selected) Color.White else FondoSegmentoGris,
                                shape = RoundedCornerShape(16.dp),
                                border = if (selected) androidx.compose.foundation.BorderStroke(2.dp, NaranjaEstudiante) else null,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .clickable { pedidosViewModel.onPagoChange(pago) }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = pago,
                                        color = if (selected) NaranjaEstudiante else Color.Black,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Sección: Resumen de precios
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal", color = Color.Gray, fontSize = 14.sp)
                        Text("S/ %.2f".format(subtotal), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Servicio", color = Color.Gray, fontSize = 14.sp)
                        Text("S/ 0.00", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(
                            text = "S/ %.2f".format(subtotal),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = NaranjaEstudiante
                        )
                    }
                }
            }

            // Acciones: Botón Confirmar pedido y Botón Seguir pidiendo
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = {
                            pedidosViewModel.confirmarPedido(
                                itemsCarrito = menuState.carrito,
                                onExito = { pedidoId ->
                                    menuViewModel.limpiarCarrito()
                                    onPedidoConfirmado(pedidoId)
                                }
                            )
                        },
                        enabled = menuState.carrito.isNotEmpty() && !confirmarState.estaCargando,
                        colors = ButtonDefaults.buttonColors(containerColor = NaranjaEstudiante),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text("Confirmar pedido", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onVolverAlMenu,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, NaranjaEstudiante),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text("Seguir pidiendo", color = NaranjaEstudiante, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
