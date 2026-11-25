package com.rau.avisoscampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rau.avisoscampus.model.Aviso
import com.rau.avisoscampus.model.Origen
import com.rau.avisoscampus.ui.theme.AzulTarjeta
import com.rau.avisoscampus.ui.theme.MoradoOrigen
import com.rau.avisoscampus.ui.theme.NaranjaOrigen

@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Historial",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )

            if (avisos.isEmpty()) {
                Text("Sin avisos todavía", style = MaterialTheme.typography.bodySmall)
            } else {
                avisos.take(20).forEach { aviso ->
                    FilaAviso(aviso)
                }
            }
        }
    }
}

@Composable
private fun FilaAviso(aviso: Aviso) {
    val colorOrigen = when (aviso.origen) {
        Origen.BOTON -> AzulTarjeta
        Origen.RECORDATORIO -> NaranjaOrigen
        Origen.MODO_AVION -> MoradoOrigen
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF1F3F7)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 7.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(shape = RoundedCornerShape(20.dp), color = colorOrigen) {
                Text(
                    text = aviso.origen.texto,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = aviso.titulo,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = aviso.texto,
                    fontSize = 10.sp,
                    color = Color(0xFF667085),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(text = aviso.hora, fontSize = 10.sp, color = Color(0xFF667085))
        }
    }
}
