package com.rau.campusgps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rau.campusgps.model.TipoError
import com.rau.campusgps.model.UbicacionUi
import com.rau.campusgps.ui.theme.CampusBlue
import com.rau.campusgps.ui.theme.CampusMuted

@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    onAbrirAjustes: () -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.errorContainer,
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surface.copy(alpha = 0.75f)) {
                Text("!", Modifier.padding(horizontal = 15.dp, vertical = 8.dp), color = CampusBlue, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Text(
                "No se pudo obtener la ubicación",
                color = MaterialTheme.colorScheme.onErrorContainer,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(error.mensaje, color = MaterialTheme.colorScheme.onErrorContainer, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
            Spacer(Modifier.height(1.dp))
            when (error.tipo) {
                TipoError.SIN_PERMISO -> OutlinedButton(onClick = onAbrirAjustes, shape = RoundedCornerShape(15.dp)) {
                    Text("Abrir ajustes", fontWeight = FontWeight.SemiBold)
                }
                TipoError.GPS_APAGADO -> OutlinedButton(onClick = onReintentar, shape = RoundedCornerShape(15.dp)) {
                    Text("Reintentar", fontWeight = FontWeight.SemiBold)
                }
                TipoError.SIN_PLAY_SERVICES -> Text(
                    "Google Play Services es necesario para continuar.",
                    color = CampusMuted,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
                TipoError.OTRO -> OutlinedButton(onClick = onReintentar, shape = RoundedCornerShape(15.dp)) {
                    Text("Reintentar", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
