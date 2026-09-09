package com.rau.registro_notas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rau.registro_notas.FilaCursoSlider
import com.rau.registro_notas.ui.theme.Registro_NotasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Registro_NotasTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        Surface(
                            color = Color(0xFF1A365D),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Registro de Notas",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                ),
                                modifier = Modifier
                                    .statusBarsPadding()
                                    .padding(horizontal = 20.dp, vertical = 16.dp)
                            )
                        }
                    }
                ) { innerPadding ->
                    PantallaNotas(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun PantallaNotas(modifier: Modifier = Modifier) {
    var notaFundamentos by remember { mutableFloatStateOf(0f) }
    var notaPOO by remember { mutableFloatStateOf(0f) }
    var notaMoviles by remember { mutableFloatStateOf(0f) }
    var notaBD by remember { mutableFloatStateOf(0f) }
    var redondear by remember { mutableStateOf(false) }
    var confirmado by remember { mutableStateOf(false) }
    val fondoGradiente = Brush.verticalGradient(
        colors = listOf(Color(0xFFEBF8FF), Color(0xFFF7FAFC), Color.White)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(fondoGradiente)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Notas del ciclo",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF1A365D)
        )
        Text(
            text = "Desliza para asignar cada nota (0 a 20)",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(12.dp))

        FilaCursoSlider("Fundamentos de Programación", 20, notaFundamentos) { notaFundamentos = it }
        FilaCursoSlider("Programación Orientada a Objetos", 25, notaPOO) { notaPOO = it }
        FilaCursoSlider("Programación en Móviles", 30, notaMoviles) { notaMoviles = it }
        FilaCursoSlider("Base de Datos", 25, notaBD) { notaBD = it }
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Redondear promedio final",
                style = MaterialTheme.typography.bodyMedium
            )
            Switch(
                checked = redondear,
                onCheckedChange = { redondear = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF2B6CB0)
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = confirmado,
                onCheckedChange = { confirmado = it },
                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF2B6CB0))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Confirmo que las notas son correctas",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {},
            enabled = confirmado,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2B6CB0),
                disabledContainerColor = Color(0xFFCBD5E0),
                disabledContentColor = Color.White
            )
        ) {
            Text("CALCULAR PROMEDIO", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Asigna las notas y confirma para calcular",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun FilaCursoSlider(
    nombreCurso: String,
    peso: Int,
    nota: Float,
    onNotaChange: (Float) -> Unit
) {
    val colorBadge = if (nota < 13f) Color(0xFFFFCDD2) else Color(0xFFBEE3F8)
    val colorTextoBadge = if (nota < 13f) Color(0xFFB71C1C) else Color(0xFF1A365D)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = nombreCurso,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF2D3748)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "($peso%)",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF2B6CB0)
                )
            }
            Surface(
                color = colorBadge,
                shape = MaterialTheme.shapes.extraSmall,
                modifier = Modifier
                    .width(36.dp)
                    .height(24.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "${nota.toInt()}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = colorTextoBadge
                    )
                }
            }
        }

        Slider(
            value = nota,
            onValueChange = onNotaChange,
            valueRange = 0f..20f,
            steps = 19,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF2B6CB0),
                activeTrackColor = Color(0xFF2B6CB0),
                inactiveTrackColor = Color(0xFFCBD5E0)
            )
        )
    }
}