package com.rau.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.rau.campusgps.model.LugarConDistancia
import com.rau.campusgps.model.UbicacionUi

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int? = null,
    masCercanoId: Int? = null,
    modifier: Modifier = Modifier
) {
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
        cameraPositionState = cameraPositionState
    ) {
        if (estado is UbicacionUi.Lista) {
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