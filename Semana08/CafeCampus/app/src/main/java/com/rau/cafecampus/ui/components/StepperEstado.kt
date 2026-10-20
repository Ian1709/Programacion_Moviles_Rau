package com.rau.cafecampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rau.cafecampus.ui.theme.NaranjaEstudiante

@Composable
fun StepperEstado(
    estadoActual: String,
    modifier: Modifier = Modifier
) {
    val pasos = listOf(
        Triple("Recibido", "Tu pedido llegó a la cocina", "PENDIENTE"),
        Triple("Preparando", "Están cocinando tu pedido", "EN_PREPARACION"),
        Triple("Listo", "Puedes pasar a recogerlo", "LISTO"),
        Triple("Entregado", "¡Buen provecho!", "ENTREGADO")
    )

    val indiceActual = when (estadoActual.uppercase()) {
        "PENDIENTE" -> 0
        "EN_PREPARACION" -> 1
        "LISTO" -> 2
        "ENTREGADO" -> 3
        else -> 0
    }

    Column(modifier = modifier.fillMaxWidth()) {
        pasos.forEachIndexed { index, (titulo, descripcion, _) ->
            val esCompletado = index < indiceActual
            val esActual = index == indiceActual
            val esUltimo = index == pasos.size - 1

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Columna del Círculo e Indicador Vertical
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    esCompletado -> Color(0xFF2E7D32) // Verde check
                                    esActual -> NaranjaEstudiante     // Naranja activo
                                    else -> Color(0xFFE0E0E0)         // Gris futuro
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when {
                                esCompletado -> "✓"
                                esActual -> "●"
                                else -> ""
                            },
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    if (!esUltimo) {
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(44.dp)
                                .background(if (index < indiceActual) Color(0xFF2E7D32) else Color(0xFFE0E0E0))
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Columna de Título y Subtítulo
                Column(modifier = Modifier.padding(top = 4.dp)) {
                    Text(
                        text = titulo,
                        fontWeight = if (esActual || esCompletado) FontWeight.Bold else FontWeight.Normal,
                        color = if (esActual) NaranjaEstudiante else if (esCompletado) Color.Black else Color.Gray,
                        fontSize = 15.sp
                    )
                    Text(
                        text = descripcion,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
