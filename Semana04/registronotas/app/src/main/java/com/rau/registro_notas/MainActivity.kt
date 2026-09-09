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
import java.util.Locale
import kotlin.math.roundToInt

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
    var mostrarResultado by remember { mutableStateOf(false) }
    var promPonderado by remember { mutableDoubleStateOf(0.0) }
    var promFinal by remember { mutableDoubleStateOf(0.0) }
    var fueRedondeado by remember { mutableStateOf(false) }
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
            onClick = {
                val ponderado = (notaFundamentos * 0.20) +
                        (notaPOO * 0.25) +
                        (notaMoviles * 0.30) +
                        (notaBD * 0.25)
                promPonderado = ponderado
                fueRedondeado = redondear
                promFinal = if (redondear) ponderado.roundToInt().toDouble() else ponderado
                mostrarResultado = true
            },
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

        if (!mostrarResultado) {
            Text(
                text = "Asigna las notas y confirma para calcular",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Promedio ponderado:   ${String.format(Locale.US, "%.2f", promPonderado)}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = Color(0xFF2D3748)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "Promedio final:  ",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF1A365D)
                        )
                        Text(
                            text = if (fueRedondeado) "${promFinal.toInt()}" else String.format(Locale.US, "%.2f", promFinal),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF1A365D)
                        )
                    }
                    if (fueRedondeado) {
                        Text(
                            text = "(redondeado)",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val (observacion, colorFondoChip, colorTextoChip) = when {
                        promFinal >= 17.0 -> Triple("EXCELENTE", Color(0xFFC8E6C9), Color(0xFF1B5E20))
                        promFinal >= 13.0 -> Triple("APROBADO", Color(0xFFDCEDC8), Color(0xFF33691E))
                        promFinal >= 10.0 -> Triple("EN RECUPERACIÓN", Color(0xFFFFECB3), Color(0xFFE65100))
                        else -> Triple("DESAPROBADO", Color(0xFFFFCDD2), Color(0xFFB71C1C))
                    }

                    Surface(
                        color = colorFondoChip,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = observacion,
                            color = colorTextoChip,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                    Text(
                        text = "Aporte por curso:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.DarkGray
                    )
                    Text(
                        text = "• Fundamentos: ${notaFundamentos.toInt()} × 20% = ${String.format(Locale.US, "%.2f", notaFundamentos * 0.20)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "• POO: ${notaPOO.toInt()} × 25% = ${String.format(Locale.US, "%.2f", notaPOO * 0.25)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "• Móviles: ${notaMoviles.toInt()} × 30% = ${String.format(Locale.US, "%.2f", notaMoviles * 0.30)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "• Base de Datos: ${notaBD.toInt()} × 25% = ${String.format(Locale.US, "%.2f", notaBD * 0.25)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
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