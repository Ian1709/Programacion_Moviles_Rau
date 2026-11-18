package com.rau.centrotareas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rau.centrotareas.model.CargaUi
import com.rau.centrotareas.model.PrimosUi
import com.rau.centrotareas.viewmodel.TareasViewModel

private val Fondo = Color(0xFFFCFAFF)
private val Azul = Color(0xFF2478A8)
private val Borde = Color(0xFFE1E7ED)
private val VerdeClaro = Color(0xFFE3F4E4)
private val RojoClaro = Color(0xFFFBE4E4)
private val FormaTarjeta = RoundedCornerShape(14.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(viewModel: TareasViewModel) {
    val toques by viewModel.toques.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val progreso by viewModel.progreso.collectAsState()
    val corriendo by viewModel.corriendo.collectAsState()
    val primos by viewModel.primos.collectAsState()
    val carga by viewModel.carga.collectAsState()
    val simularError by viewModel.simularError.collectAsState()
    var hasta by rememberSaveable { mutableStateOf("500000") }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(56.dp),
                title = {
                    Text("CentroTareas", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Azul)
            )
        },
        containerColor = Fondo
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TarjetaLaboratorio {
                Text("Tarea con progreso", color = Azul, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(Modifier.height(4.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    BotonPrimario(onClick = viewModel::tocar) {
                        Text("Tócame: $toques", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BotonPrimario(onClick = viewModel::tareaLarga, enabled = !corriendo) {
                        Text("Iniciar", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(Modifier.width(8.dp))
                    BotonContorno(onClick = viewModel::cancelar, enabled = corriendo) {
                        Text("Cancelar", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                LinearProgressIndicator(
                    progress = { progreso },
                    modifier = Modifier.fillMaxWidth().height(5.dp).clip(CircleShape),
                    color = Azul,
                    trackColor = Color(0xFFDDE2E5)
                )
                Text("${(progreso * 100).toInt()} %", modifier = Modifier.align(Alignment.CenterHorizontally), fontSize = 11.sp)
                Text(mensaje, modifier = Modifier.align(Alignment.CenterHorizontally), fontSize = 12.sp)
            }

            TarjetaLaboratorio {
                Text("Contar números primos", color = Azul, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                OutlinedTextField(
                    value = hasta,
                    onValueChange = { hasta = it.filter(Char::isDigit) },
                    label = { Text("Hasta el número", fontSize = 12.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BotonPrimario(onClick = { hasta.toIntOrNull()?.takeIf { it >= 2 }?.let(viewModel::contar) }) {
                        Text("Contar", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(Modifier.width(8.dp))
                    BotonContorno(onClick = viewModel::cancelarPrimos, enabled = primos is PrimosUi.Calculando) {
                        Text("Cancelar", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                when (val estado = primos) {
                    PrimosUi.Inicial -> Unit
                    is PrimosUi.Calculando -> Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        LinearProgressIndicator(
                            progress = { estado.progreso },
                            modifier = Modifier.fillMaxWidth().height(5.dp).clip(CircleShape),
                            color = Azul,
                            trackColor = Color(0xFFDDE2E5)
                        )
                        Text("Calculando... ${(estado.progreso * 100).toInt()} %", fontSize = 11.sp)
                    }
                    PrimosUi.Cancelado -> Text("Cancelado", color = MaterialTheme.colorScheme.error, fontSize = 11.sp)
                    is PrimosUi.Listo -> CajaResultado(
                        "Primos hasta $hasta: ${estado.cantidad}\nTiempo: ${estado.milisegundos} ms",
                        VerdeClaro
                    )
                }
            }

            TarjetaLaboratorio {
                Text("Dos cargas", color = Azul, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.width(36.dp).height(24.dp), contentAlignment = Alignment.CenterStart) {
                        Switch(
                            checked = simularError,
                            onCheckedChange = viewModel::cambiarSimularError,
                            modifier = Modifier.scale(0.72f)
                        )
                    }
                    Text("Simular error", fontSize = 12.sp)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BotonContorno(onClick = viewModel::cargarUnaPorUna) { Text("Una por una", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                    Spacer(Modifier.width(8.dp))
                    BotonPrimario(onClick = viewModel::cargarALaVez) { Text("A la vez", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                }
                when (val estado = carga) {
                    CargaUi.Inicial -> Unit
                    CargaUi.Cargando -> CircularProgressIndicator(
                        modifier = Modifier.size(24.dp).align(Alignment.CenterHorizontally),
                        color = Azul,
                        strokeWidth = 2.dp
                    )
                    is CargaUi.Exito -> CajaResultado(
                        "Modo: ${estado.modo}\nPerfil: ${estado.perfil}\nPedidos: ${estado.pedidos}\nTiempo: ${estado.milisegundos} ms",
                        VerdeClaro
                    )
                    is CargaUi.Error -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            estado.mensaje,
                            modifier = Modifier.fillMaxWidth().background(RojoClaro, RoundedCornerShape(8.dp)).padding(10.dp),
                            fontSize = 11.sp
                        )
                        BotonContorno(onClick = viewModel::reintentar) { Text("Reintentar", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaLaboratorio(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().border(1.dp, Borde, FormaTarjeta),
        shape = FormaTarjeta,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 9.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            content = content
        )
    }
}

@Composable
private fun BotonPrimario(
    onClick: () -> Unit,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.height(34.dp),
        shape = CircleShape,
        contentPadding = PaddingValues(horizontal = 15.dp, vertical = 0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Azul)
    ) {
        content()
    }
}

@Composable
private fun BotonContorno(
    onClick: () -> Unit,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.height(34.dp),
        shape = CircleShape,
        contentPadding = PaddingValues(horizontal = 15.dp, vertical = 0.dp)
    ) {
        content()
    }
}

@Composable
private fun CajaResultado(texto: String, color: Color) {
    Text(
        text = texto,
        modifier = Modifier.fillMaxWidth().background(color, RoundedCornerShape(9.dp)).padding(horizontal = 9.dp, vertical = 8.dp),
        fontSize = 11.sp,
        lineHeight = 14.sp,
        color = Color(0xFF2E5E39)
    )
}
