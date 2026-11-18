package com.tecsup.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.tecsup.campusgps.model.UbicacionUi
import com.tecsup.campusgps.viewmodel.MapaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val context = LocalContext.current
    val estado by vm.estado.collectAsState()

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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MapaUbicacion(
                estado = estado,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            when (val actual = estado) {
                UbicacionUi.SinBuscar -> Text("Pulsa el botón para obtener tu ubicación.")
                UbicacionUi.Buscando -> CircularProgressIndicator()
                is UbicacionUi.Lista -> Text(
                    "Latitud: ${actual.posicion.latitud}\nLongitud: ${actual.posicion.longitud}"
                )
                is UbicacionUi.Error -> Text(
                    text = actual.mensaje,
                    color = MaterialTheme.colorScheme.error
                )
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
        }
    }
}