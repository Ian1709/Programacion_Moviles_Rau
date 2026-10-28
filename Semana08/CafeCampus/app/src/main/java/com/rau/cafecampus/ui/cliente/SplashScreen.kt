package com.rau.cafecampus.ui.cliente

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rau.cafecampus.ui.theme.NaranjaEstudiante
import com.rau.cafecampus.viewmodel.SesionViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    sesionViewModel: SesionViewModel,
    onNavegarLogin: () -> Unit,
    onNavegarCliente: () -> Unit,
    onNavegarAdmin: () -> Unit
) {
    val isLoggedIn by sesionViewModel.isLoggedIn.collectAsState(initial = false)
    val userRole by sesionViewModel.userRole.collectAsState(initial = "")

    val scale = remember { Animatable(0.6f) }

    LaunchedEffect(isLoggedIn, userRole) {
        scale.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(durationMillis = 800)
        )
        delay(1200)

        if (isLoggedIn) {
            if (userRole == "ADMIN") {
                onNavegarAdmin()
            } else {
                onNavegarCliente()
            }
        } else {
            onNavegarLogin()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        NaranjaEstudiante.copy(alpha = 0.15f),
                        MaterialTheme.colorScheme.background
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.scale(scale.value)
        ) {
            // Badge Circular con Icono de la Cafetería
            Surface(
                shape = CircleShape,
                color = NaranjaEstudiante,
                shadowElevation = 14.dp,
                modifier = Modifier.size(108.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "☕",
                        fontSize = 52.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Título Principal en Naranja Estudiante de Alto Contraste
            Text(
                text = "CafeCampus",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = NaranjaEstudiante,
                fontSize = 36.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Subtítulo con Color de Texto Destacado
            Text(
                text = "Cafetería Universitaria TECSUP",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            CircularProgressIndicator(
                color = NaranjaEstudiante,
                strokeWidth = 3.5.dp,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}
