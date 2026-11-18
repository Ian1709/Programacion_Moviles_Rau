package com.tecsup.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.tecsup.campusgps.model.UbicacionUi
import com.tecsup.campusgps.util.formatearDistancia
import com.tecsup.campusgps.viewmodel.MapaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel, onAbrirAjustes: () -> Unit) {
    val context = LocalContext.current
    val estado by vm.estado.collectAsState()
    val lugares by vm.lugaresVisibles.collectAsState()
    val categoriaSeleccionada by vm.categoriaSeleccionada.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()

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

    Scaffold(
        topBar = { TopAppBar(title = { Text("CampusGPS") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val categorias = remember {
                listOf("Estudio", "Comida", "Eventos", "Servicios")
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
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

            Button(
                onClick = {
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
                },
                enabled = estado !is UbicacionUi.Buscando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Obtener mi ubicación")
            }

            when (val actual = estado) {
                UbicacionUi.SinBuscar -> Text("Pulsa el botón para obtener tu ubicación.")
                UbicacionUi.Buscando -> CircularProgressIndicator()
                is UbicacionUi.Lista -> Text(
                    "Latitud: ${actual.posicion.latitud}\nLongitud: ${actual.posicion.longitud}"
                )
                is UbicacionUi.Error -> MensajeError(
                    error = actual,
                    onAbrirAjustes = onAbrirAjustes,
                    onReintentar = vm::buscar
                )
            }

            OutlinedButton(
                onClick = { vm.seleccionar(masCercano?.id) },
                enabled = masCercano != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ir al más cercano")
            }
            masCercano?.let { lugar ->
                val distanciaMasCercana =
                    lugares.firstOrNull { it.lugar.id == lugar.id }?.metros
                if (distanciaMasCercana != null) {
                    Text(
                        "Más cercano: ${lugar.nombre} (${formatearDistancia(distanciaMasCercana)})"
                    )
                }
            }
            MapaUbicacion(
                estado = estado,
                lugares = lugares,
                seleccionado = seleccionado,
                masCercanoId = masCercano?.id,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            ListaLugares(
                lugares = lugares,
                seleccionado = seleccionado,
                onClick = vm::seleccionar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }
    }
}
