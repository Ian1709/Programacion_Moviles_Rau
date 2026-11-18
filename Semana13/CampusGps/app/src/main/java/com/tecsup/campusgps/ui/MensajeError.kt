package com.tecsup.campusgps.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.campusgps.model.TipoError
import com.tecsup.campusgps.model.UbicacionUi

@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    onAbrirAjustes: () -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = error.mensaje,
            color = MaterialTheme.colorScheme.error
        )

        when (error.tipo) {
            TipoError.SIN_PERMISO -> Button(onClick = onAbrirAjustes) {
                Text("Abrir ajustes")
            }
            TipoError.GPS_APAGADO -> Button(onClick = onReintentar) {
                Text("Reintentar")
            }
            TipoError.SIN_PLAY_SERVICES -> Unit
            TipoError.OTRO -> Button(onClick = onReintentar) {
                Text("Reintentar")
            }
        }
    }
}