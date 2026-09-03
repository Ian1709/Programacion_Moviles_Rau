package com.rau.lab03registroproducto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rau.lab03registroproducto.ui.theme.Lab03RegistroProductoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab03RegistroProductoTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Registro de Producto",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .statusBarsPadding()
                                    .padding(horizontal = 16.dp, vertical = 16.dp)
                            )
                        }
                    }
                ) { innerPadding ->
                    PantallaRegistro(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun PantallaRegistro(modifier: Modifier = Modifier) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }

    // Estados de control para la validación y visibilidad
    var mostrarResumen by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Encabezado
        Column {
            Text(
                text = "Nuevo producto",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Completa los datos y presiona Agregar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Campo: Nombre
        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                mensajeError = ""
                mostrarResumen = false
            },
            label = { Text("Nombre del producto") },
            singleLine = true,
            isError = mensajeError.isNotEmpty() && nombre.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )

        // Fila: Precio y Cantidad
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = precio,
                onValueChange = {
                    precio = it
                    mensajeError = ""
                    mostrarResumen = false
                },
                label = { Text("Precio (S/)") },
                singleLine = true,
                isError = mensajeError.isNotEmpty() && (precio.toDoubleOrNull() == null || precio.toDoubleOrNull()!! <= 0.0),
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = cantidad,
                onValueChange = {
                    cantidad = it
                    mensajeError = ""
                    mostrarResumen = false
                },
                label = { Text("Cantidad") },
                singleLine = true,
                isError = mensajeError.isNotEmpty() && (cantidad.toIntOrNull() == null || cantidad.toIntOrNull()!! <= 0),
                modifier = Modifier.weight(1f)
            )
        }

        // Fila de Botones: AGREGAR PRODUCTO y LIMPIAR
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    val p = precio.toDoubleOrNull()
                    val c = cantidad.toIntOrNull()

                    when {
                        nombre.isBlank() -> {
                            mensajeError = "Debes ingresar el nombre del producto."
                            mostrarResumen = false
                        }
                        p == null || p <= 0.0 -> {
                            mensajeError = "Ingresa un precio válido mayor a 0."
                            mostrarResumen = false
                        }
                        c == null || c <= 0 -> {
                            mensajeError = "Ingresa una cantidad entera mayor a 0."
                            mostrarResumen = false
                        }
                        else -> {
                            mensajeError = ""
                            mostrarResumen = true
                        }
                    }
                },
                modifier = Modifier.weight(1.5f)
            ) {
                Text("AGREGAR PRODUCTO")
            }

            // Botón Limpiar para resetear el formulario
            OutlinedButton(
                onClick = {
                    nombre = ""
                    precio = ""
                    cantidad = ""
                    mensajeError = ""
                    mostrarResumen = false
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("LIMPIAR")
            }
        }

        // Mensaje de Error en Rojo
        if (mensajeError.isNotEmpty()) {
            Text(
                text = "⚠ $mensajeError",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }

        // Estado inicial
        if (!mostrarResumen && mensajeError.isEmpty()) {
            Text(
                text = "Aún no has registrado ningún producto",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Card de Resumen y Confirmación
        if (mostrarResumen) {
            val precioNum = precio.toDoubleOrNull() ?: 0.0
            val cantidadNum = cantidad.toIntOrNull() ?: 0
            val importeTotal = precioNum * cantidadNum

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Precio: S/ ${String.format("%.2f", precioNum)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Cantidad: $cantidadNum",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Importe total: S/ ${String.format("%.2f", importeTotal)}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "✓ Producto registrado correctamente",
                color = Color(0xFF2E7D32),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}