package com.rau.avisoscampus.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.rau.avisoscampus.model.PermisoUi
import com.rau.avisoscampus.notificacion.Notificador
import com.rau.avisoscampus.ui.theme.GrisEstado
import com.rau.avisoscampus.ui.theme.RojoEstado
import com.rau.avisoscampus.ui.theme.VerdeEstado
import com.rau.avisoscampus.viewmodel.AvisosViewModel

@Composable
fun PantallaAvisos(vm: AvisosViewModel) {
    val contexto = LocalContext.current
    val permiso by vm.permiso.collectAsState()
    val faltan by vm.faltan.collectAsState()
    val lanzador = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = vm::permisoRespondido
    )

    LaunchedEffect(Unit) {
        if (Notificador.tienePermiso(contexto)) vm.permisoRespondido(true)
    }

    LaunchedEffect(vm) {
        vm.eventos.collect { aviso -> Notificador.mostrar(contexto, aviso) }
    }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TarjetaSeccion("Permiso de notificaciones") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Estado:", style = MaterialTheme.typography.bodyMedium)
                EtiquetaPermiso(permiso)
            }
            Button(
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        lanzador.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        vm.permisoRespondido(true)
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Permitir notificaciones")
            }
        }

        TarjetaSeccion("Avisos") {
            Button(
                onClick = vm::avisarAhora,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Mostrar aviso")
            }
        }
        TarjetaSeccion("Recordatorio") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { vm.recordarEn() },
                    enabled = faltan == null
                ) {
                    Text("Recordar en 10 s")
                }
                OutlinedButton(
                    onClick = vm::cancelarRecordatorio,
                    enabled = faltan != null
                ) {
                    Text("Cancelar")
                }
            }
            faltan?.let { segundos ->
                Text(
                    text = "Faltan $segundos s",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        TarjetaSeccion("Receptor de anuncios") { TextoPendiente("Se habilitará en el paso 6.") }
        TarjetaSeccion("Historial") { TextoPendiente("Se habilitará en el paso 7.") }
    }
}

@Composable
private fun EtiquetaPermiso(permiso: PermisoUi) {
    val (texto, color) = when (permiso) {
        PermisoUi.SinPedir -> "Sin pedir" to GrisEstado
        PermisoUi.Concedido -> "Permitido" to VerdeEstado
        PermisoUi.Denegado -> "Denegado" to RojoEstado
    }
    Text(text = texto, color = color, style = MaterialTheme.typography.labelLarge)
}

@Composable
internal fun TarjetaSeccion(
    titulo: String,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(titulo, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
            contenido(this)
        }
    }
}

@Composable
private fun TextoPendiente(texto: String) {
    Text(texto, style = MaterialTheme.typography.bodySmall, color = GrisEstado)
}
