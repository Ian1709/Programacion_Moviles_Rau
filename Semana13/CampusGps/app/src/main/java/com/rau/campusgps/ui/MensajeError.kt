package com.rau.campusgps.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rau.campusgps.model.TipoError
import com.rau.campusgps.model.UbicacionUi

@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    onAbrirAjustes: () -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        color = MaterialTheme.colorScheme.errorContainer,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = error.mensaje,
                color = MaterialTheme.colorScheme.onErrorContainer,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(2.dp))
            when (error.tipo) {
                TipoError.SIN_PERMISO -> OutlinedButton(
                    onClick = onAbrirAjustes,
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Abrir ajustes")
                }
                TipoError.GPS_APAGADO -> OutlinedButton(
                    onClick = onReintentar,
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Reintentar")
                }
                TipoError.SIN_PLAY_SERVICES -> Unit
                TipoError.OTRO -> OutlinedButton(
                    onClick = onReintentar,
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Reintentar")
                }
            }
        }
    }
}
