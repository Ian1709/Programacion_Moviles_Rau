package com.rau.cafecampus.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rau.cafecampus.ui.theme.NaranjaEstudiante

@Composable
fun EstadoChip(
    estado: String,
    modifier: Modifier = Modifier
) {
    val (colorFondo, colorTexto, textoLegible) = when (estado.uppercase()) {
        "PENDIENTE" -> Triple(Color(0xFFFFF3E0), NaranjaEstudiante, "PENDIENTE")
        "EN_PREPARACION" -> Triple(Color(0xFFE3F2FD), Color(0xFF1976D2), "EN PREPARACIÓN")
        "LISTO" -> Triple(Color(0xE3E8F5E9), Color(0xFF2E7D32), "¡LISTO!")
        "ENTREGADO" -> Triple(Color(0xFFF5F5F5), Color(0xFF616161), "ENTREGADO")
        "CANCELADO" -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), "CANCELADO")
        else -> Triple(Color(0xFFF5F5F5), Color(0xFF616161), estado)
    }

    Surface(
        color = colorFondo,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Text(
            text = textoLegible,
            color = colorTexto,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
