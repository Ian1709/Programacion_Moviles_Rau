package com.tecsup.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.CampoTexto
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * PantallaDatosEntrega (Hito 8): Formulario de datos de envío con validación en rojo.
 */
@Composable
fun PantallaDatosEntrega(
    total: Double,
    onVolver: () -> Unit,
    onConfirmarPedido: (direccion: String, referencia: String, telefono: String, nombreRecibe: String) -> Unit
) {
    var direccion by remember { mutableStateOf("") }
    var referencia by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var nombreRecibe by remember { mutableStateOf("") }

    var errorDireccion by remember { mutableStateOf(false) }
    var errorReferencia by remember { mutableStateOf(false) }
    var errorTelefono by remember { mutableStateOf(false) }
    var errorNombre by remember { mutableStateOf(false) }
    var mensajeErrorGlobal by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        EncabezadoEntrega(onVolver = onVolver)

        Spacer(Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocalShipping,
                    contentDescription = null,
                    tint = VerdeBodega,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.size(12.dp))
                Column {
                    Text(
                        text = "Datos de Despacho",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Ingresa la dirección donde entregaremos tu pedido",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        if (mensajeErrorGlobal != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = mensajeErrorGlobal ?: "",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(Modifier.height(16.dp))
        }

        CampoTexto(
            etiqueta = "Dirección de entrega *",
            valor = direccion,
            onValorCambia = {
                direccion = it
                errorDireccion = false
                mensajeErrorGlobal = null
            },
            placeholder = "Av. Javier Prado Este 1234, Dpto 502",
            esError = errorDireccion,
            mensajeError = if (errorDireccion) "La dirección es obligatoria" else null
        )

        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Referencia / Instrucciones *",
            valor = referencia,
            onValorCambia = {
                referencia = it
                errorReferencia = false
                mensajeErrorGlobal = null
            },
            placeholder = "Frente al parque principal / Portón verde",
            esError = errorReferencia,
            mensajeError = if (errorReferencia) "La referencia es obligatoria" else null
        )

        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Teléfono de contacto *",
            valor = telefono,
            onValorCambia = {
                telefono = it
                errorTelefono = false
                mensajeErrorGlobal = null
            },
            placeholder = "987 654 321",
            teclado = KeyboardType.Phone,
            esError = errorTelefono,
            mensajeError = if (errorTelefono) "El teléfono de contacto es obligatorio" else null
        )

        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Nombre de quien recibe *",
            valor = nombreRecibe,
            onValorCambia = {
                nombreRecibe = it
                errorNombre = false
                mensajeErrorGlobal = null
            },
            placeholder = "Juan Pérez",
            esError = errorNombre,
            mensajeError = if (errorNombre) "El nombre de quien recibe es obligatorio" else null
        )

        Spacer(Modifier.height(24.dp))

        HorizontalDivider()

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total a Pagar:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "S/ %.2f".format(total),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = VerdeBodega
            )
        }

        Spacer(Modifier.height(20.dp))

        BotonPrimario(
            texto = "Confirmar y Pagar",
            onClick = {
                val dirTrim = direccion.trim()
                val refTrim = referencia.trim()
                val telTrim = telefono.trim()
                val nomTrim = nombreRecibe.trim()

                var valido = true
                if (dirTrim.isEmpty()) {
                    errorDireccion = true
                    valido = false
                }
                if (refTrim.isEmpty()) {
                    errorReferencia = true
                    valido = false
                }
                if (telTrim.isEmpty()) {
                    errorTelefono = true
                    valido = false
                }
                if (nomTrim.isEmpty()) {
                    errorNombre = true
                    valido = false
                }

                if (!valido) {
                    mensajeErrorGlobal = "Complete todos los campos obligatorios marcados en rojo."
                    return@BotonPrimario
                }

                mensajeErrorGlobal = null
                onConfirmarPedido(dirTrim, refTrim, telTrim, nomTrim)
            }
        )

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun EncabezadoEntrega(onVolver: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onVolver) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver"
            )
        }
        Text(
            text = "Datos de Entrega",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PantallaDatosEntregaPreview() {
    BodegaTheme {
        PantallaDatosEntrega(
            total = 28.50,
            onVolver = {},
            onConfirmarPedido = { _, _, _, _ -> }
        )
    }
}
