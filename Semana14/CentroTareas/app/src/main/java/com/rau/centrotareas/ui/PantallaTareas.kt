package com.rau.centrotareas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.rau.centrotareas.model.PrimosUi
import com.rau.centrotareas.viewmodel.TareasViewModel

private val Fondo = Color(0xFFFCFAFF)
private val Azul = Color(0xFF2478A8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(viewModel: TareasViewModel) {
    val toques by viewModel.toques.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val progreso by viewModel.progreso.collectAsState()
    val corriendo by viewModel.corriendo.collectAsState()
    val primos by viewModel.primos.collectAsState()
    var hasta by rememberSaveable { mutableStateOf("500000") }
    Scaffold(topBar = { SmallTopAppBar(title = { Text("CentroTareas", color = Color.White) }, colors = androidx.compose.material3.TopAppBarDefaults.smallTopAppBarColors(containerColor = Azul)) }, containerColor = Fondo) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(12.dp)) {
                Column(Modifier.fillMaxWidth().padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Tarea con progreso", color = Azul, fontSize = 14.sp, modifier = Modifier.align(Alignment.Start))
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = viewModel::tocar) { Text("Tócame: $toques", fontSize = 11.sp) }
                        Button(onClick = viewModel::tareaLarga, enabled = !corriendo) { Text("Iniciar", fontSize = 11.sp) }
                        OutlinedButton(onClick = viewModel::cancelar, enabled = corriendo) { Text("Cancelar", fontSize = 11.sp) }
                    }
                    LinearProgressIndicator(progress = { progreso }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
                    Text("${(progreso * 100).toInt()} %", fontSize = 12.sp)
                    Text(mensaje, fontSize = 12.sp)
                }
            }
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(12.dp)) {
                Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Contar números primos", color = Azul, fontSize = 14.sp)
                    OutlinedTextField(value = hasta, onValueChange = { hasta = it.filter(Char::isDigit) }, label = { Text("Hasta el número") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.fillMaxWidth())
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { hasta.toIntOrNull()?.takeIf { it >= 2 }?.let(viewModel::contar) }) { Text("Contar", fontSize = 11.sp) }
                        OutlinedButton(onClick = viewModel::cancelarPrimos, enabled = primos == PrimosUi.Calculando) { Text("Cancelar", fontSize = 11.sp) }
                    }
                    when (val estado = primos) {
                        PrimosUi.Inicial -> Unit
                        PrimosUi.Calculando -> Text("Calculando...", fontSize = 12.sp)
                        PrimosUi.Cancelado -> Text("Cancelado", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                        is PrimosUi.Listo -> ResultBox("Primos hasta $hasta: ${estado.cantidad}\\nTiempo: ${estado.milisegundos} ms", Color(0xFFE3F4E4))
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultBox(text: String, color: Color) {
    Text(text, modifier = Modifier.fillMaxWidth().background(color, RoundedCornerShape(8.dp)).padding(10.dp), fontSize = 12.sp)
}
