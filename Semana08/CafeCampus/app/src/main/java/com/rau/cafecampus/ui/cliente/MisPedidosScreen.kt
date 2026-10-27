package com.rau.cafecampus.ui.cliente

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rau.cafecampus.ui.components.CafeBottomBar
import com.rau.cafecampus.ui.components.ClienteTab
import com.rau.cafecampus.ui.components.PedidoCard
import com.rau.cafecampus.ui.theme.NaranjaEstudiante
import com.rau.cafecampus.viewmodel.MenuViewModel
import com.rau.cafecampus.viewmodel.PedidosClienteViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisPedidosScreen(
    pedidosViewModel: PedidosClienteViewModel,
    menuViewModel: MenuViewModel,
    onVolverAlMenu: () -> Unit,
    onVerSeguimiento: (Long) -> Unit,
    onCerrarSesion: () -> Unit
) {
    val misPedidos by pedidosViewModel.misPedidos.collectAsState()
    var tabSeleccionada by remember { mutableIntStateOf(0) } // 0: Activos, 1: Historial
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val pedidosFiltrados = misPedidos.filter { item ->
        val estado = item.pedido.estado.uppercase()
        if (tabSeleccionada == 0) {
            estado in listOf("PENDIENTE", "EN_PREPARACION", "LISTO")
        } else {
            estado in listOf("ENTREGADO", "CANCELADO")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mis pedidos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onVolverAlMenu) {
                        Text("‹", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            CafeBottomBar(
                tabActual = ClienteTab.PEDIDOS,
                onTabSeleccionada = { tab ->
                    when (tab) {
                        ClienteTab.MENU -> onVolverAlMenu()
                        ClienteTab.PEDIDOS -> {}
                        ClienteTab.PERFIL -> onCerrarSesion()
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Segmented Control Tabs (Activos | Historial)
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
                    // Pestaña Activos
                    Surface(
                        color = if (tabSeleccionada == 0) MaterialTheme.colorScheme.surface else Color.Transparent,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .clickable { tabSeleccionada = 0 }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "Activos",
                                color = if (tabSeleccionada == 0) NaranjaEstudiante else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = if (tabSeleccionada == 0) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        }
                    }

                    // Pestaña Historial
                    Surface(
                        color = if (tabSeleccionada == 1) MaterialTheme.colorScheme.surface else Color.Transparent,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .clickable { tabSeleccionada = 1 }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "Historial",
                                color = if (tabSeleccionada == 1) NaranjaEstudiante else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = if (tabSeleccionada == 1) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 14.sp
                            )
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
                    Text(
                        text = if (tabSeleccionada == 0) "No tienes pedidos activos" else "No tienes pedidos en el historial",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(pedidosFiltrados, key = { it.pedido.id }) { pedidoConDetalle ->
                        PedidoCard(
                            pedidoConDetalle = pedidoConDetalle,
                            onClick = { onVerSeguimiento(pedidoConDetalle.pedido.id) },
                            onRepetirPedido = {
                                pedidosViewModel.repetirPedido(
                                    pedidoConDetalle = pedidoConDetalle,
                                    menuViewModel = menuViewModel,
                                    onResultado = { mensaje ->
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar(mensaje)
                                        }
                                    }
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
