package com.rau.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.rau.campusgps.model.UbicacionUi
import com.rau.campusgps.util.formatearDistancia
import com.rau.campusgps.viewmodel.MapaViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel, onAbrirAjustes: () -> Unit) {
    val context = LocalContext.current
    val estado by vm.estado.collectAsState()
    val lugares by vm.lugaresVisibles.collectAsState()
    val categoriaSeleccionada by vm.categoriaSeleccionada.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()
    val radioMetros by vm.radioMetros.collectAsState()

    val permisoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permisos ->
        val concedido = permisos[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permisos[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (concedido) vm.buscar() else vm.permisoDenegado()
    }

    val tienePermiso = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val solicitarUbicacion = {
        if (tienePermiso) {
            vm.buscar()
        } else {
            permisoLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CampusGPS", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF176B98)
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val estadoError = estado as? UbicacionUi.Error
            if (estadoError != null) {
                Spacer(modifier = Modifier.height(4.dp))
                BotonUbicacion(
                    habilitado = true,
                    onClick = solicitarUbicacion
                )
                MensajeError(
                    error = estadoError,
                    onAbrirAjustes = onAbrirAjustes,
                    onReintentar = vm::buscar,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
            } else {
                val categorias = listOf("Estudio", "Comida", "Eventos", "Servicios")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FilterChip(
                        selected = categoriaSeleccionada == null,
                        onClick = { vm.filtrarPorCategoria(null) },
                        label = { Text("Todos") }
                    )
                    categorias.forEach { categoria ->
                        FilterChip(
                            selected = categoriaSeleccionada == categoria,
                            onClick = { vm.filtrarPorCategoria(categoria) },
                            label = { Text(categoria) }
                        )
                    }
                }

                MapaUbicacion(
                    estado = estado,
                    lugares = lugares,
                    seleccionado = seleccionado,
                    masCercanoId = masCercano?.id,
                    radioMetros = radioMetros,
                    modifier = Modifier
                        .weight(1.35f)
                        .fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Radio:",
                        modifier = Modifier.padding(end = 4.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                    listOf(100, 200, 500).forEach { radio ->
                        FilterChip(
                            selected = radioMetros == radio,
                            onClick = { vm.cambiarRadio(radio) },
                            label = { Text("${radio} m") }
                        )
                    }
                }

                BotonUbicacion(
                    habilitado = estado !is UbicacionUi.Buscando,
                    onClick = solicitarUbicacion
                )

                when (val actual = estado) {
                    UbicacionUi.SinBuscar -> Text(
                        "Pulsa el botón para empezar",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                    UbicacionUi.Buscando -> CircularProgressIndicator()
                    is UbicacionUi.Lista -> Text(
                        text = "Latitud: ${"%.4f".format(Locale.US, actual.posicion.latitud)}\n" +
                            "Longitud: ${"%.4f".format(Locale.US, actual.posicion.longitud)}",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                    is UbicacionUi.Error -> Unit
                }

                OutlinedButton(
                    onClick = { vm.seleccionar(masCercano?.id) },
                    enabled = masCercano != null,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.height(38.dp)
                ) {
                    Text("Ir al más cercano")
                }
                masCercano?.let { lugar ->
                    val distancia = lugares.firstOrNull { it.lugar.id == lugar.id }?.metros
                    if (distancia != null) {
                        Text(
                            "Más cercano: ${lugar.nombre} (${formatearDistancia(distancia)})",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }

                if (estado is UbicacionUi.Lista) {
                    ListaLugares(
                        lugares = lugares,
                        seleccionado = seleccionado,
                        onClick = vm::seleccionar,
                        masCercanoId = masCercano?.id,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.9f)
                    )
                }
            }
        }
    }
}

@Composable
private fun BotonUbicacion(
    habilitado: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = habilitado,
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF176B98),
            contentColor = Color.White
        ),
        modifier = Modifier
            .widthIn(min = 190.dp)
            .height(42.dp)
    ) {
        if (habilitado) Text("Obtener mi ubicación") else CircularProgressIndicator(
            modifier = Modifier.height(20.dp),
            color = Color.White,
            strokeWidth = 2.dp
        )
    }
}
