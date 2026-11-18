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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        }
    }
}
