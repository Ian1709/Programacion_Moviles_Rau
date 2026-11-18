package com.rau.campusgps.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PantallaPresentacion(onContinuar: () -> Unit) {
    val colores = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(colores.primaryContainer, colores.background, colores.background)
                )
            )
            .padding(horizontal = 30.dp, vertical = 36.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier
                    .size(132.dp)
                    .shadow(18.dp, CircleShape),
                shape = CircleShape,
                color = colores.primary
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "⌖",
                        color = colores.onPrimary,
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Light
                    )
                }
            }

            Spacer(Modifier.height(34.dp))
            Text(
                text = "CampusGPS",
                color = colores.onBackground,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Tu campus, más cerca",
                color = colores.primary,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Encuentra los lugares del campus y descubre cuál está más cerca de ti.",
                color = colores.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(38.dp))
            Button(
                onClick = onContinuar,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colores.primary,
                    contentColor = colores.onPrimary
                ),
                modifier = Modifier
                    .height(54.dp)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = "Explorar campus  →",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
