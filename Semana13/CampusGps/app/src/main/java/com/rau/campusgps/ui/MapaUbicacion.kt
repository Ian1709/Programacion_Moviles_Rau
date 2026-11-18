package com.rau.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import com.rau.campusgps.model.LugarConDistancia
import com.rau.campusgps.model.UbicacionUi

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    temaOscuro: Boolean,
    lugares: List<LugarConDistancia>,
    seleccionado: Int? = null,
    masCercanoId: Int? = null,
    radioMetros: Int = 100,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val estiloOscuro = remember(context) {
        MapStyleOptions.loadRawResourceStyle(context, com.rau.campusgps.R.raw.map_style_dark)
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(-12.0464, -77.0428),
            12f
        )
    }

    LaunchedEffect(seleccionado) {
        val lugarSeleccionado = lugares.firstOrNull { it.lugar.id == seleccionado }?.lugar
        if (lugarSeleccionado != null) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    LatLng(lugarSeleccionado.latitud, lugarSeleccionado.longitud),
                    17f
                )
            )
        }
    }

    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    LatLng(estado.posicion.latitud, estado.posicion.longitud),
                    16f
                ),
                durationMs = 1_000
            )
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            mapStyleOptions = if (temaOscuro) estiloOscuro else null
        )
    ) {
        if (estado is UbicacionUi.Lista) {
            Circle(
                center = LatLng(estado.posicion.latitud, estado.posicion.longitud),
                radius = radioMetros.toDouble(),
                fillColor = Color(0x332196F3),
                strokeColor = Color(0xFF2196F3),
                strokeWidth = 2f
            )
            Marker(
                state = MarkerState(
                    position = LatLng(estado.posicion.latitud, estado.posicion.longitud)
                ),
                title = "Estás aquí"
            )
        }

        lugares.forEach { lugarConDistancia ->
            key(lugarConDistancia.lugar.id) {
                val lugar = lugarConDistancia.lugar
                Marker(
                    state = MarkerState(
                        position = LatLng(lugar.latitud, lugar.longitud)
                    ),
                    title = lugar.nombre,
                    snippet = lugar.categoria,
                    icon = BitmapDescriptorFactory.defaultMarker(
                        if (lugar.id == masCercanoId) BitmapDescriptorFactory.HUE_GREEN else BitmapDescriptorFactory.HUE_AZURE
                    )
                )
            }
        }
    }
}
