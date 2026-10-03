package com.tecsup.mibodega.ui.cliente.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.CampoTexto
import com.tecsup.mibodega.ui.theme.AzulEnlace
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.VerdeBodega

// Credenciales fijas en código para la validación
private const val USUARIO_CORREO = "admin@mibodega.com"
private const val USUARIO_TELEFONO = "987654321"
private const val PASSWORD_CORRECTA = "123456"

/**
 * Pantalla de Login con validación contra credenciales fijas en código.
 */
@Composable
fun PantallaLogin(
    onVolver: () -> Unit,
    onLoginExitoso: () -> Unit,
    onIrARegistro: () -> Unit
) {
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var errorUsuario by remember { mutableStateOf(false) }
    var errorPassword by remember { mutableStateOf(false) }
    var mensajeErrorGlobal by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        EncabezadoLogin(onVolver = onVolver)

        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Ícono de login",
                tint = VerdeBodega,
                modifier = Modifier.size(72.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "💡 Credenciales de prueba:",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "• Correo: $USUARIO_CORREO o Teléfono: $USUARIO_TELEFONO\n• Contraseña: $PASSWORD_CORRECTA",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
            etiqueta = "Correo o Teléfono",
            valor = usuario,
            onValorCambia = {
                usuario = it
                errorUsuario = false
                mensajeErrorGlobal = null
            },
            placeholder = "admin@mibodega.com / 987654321",
            teclado = KeyboardType.Email,
            esError = errorUsuario,
            mensajeError = if (errorUsuario && usuario.isBlank()) "Campo obligatorio" else null
        )

        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Contraseña",
            valor = password,
            onValorCambia = {
                password = it
                errorPassword = false
                mensajeErrorGlobal = null
            },
            placeholder = "••••••",
            teclado = KeyboardType.Password,
            esError = errorPassword,
            mensajeError = if (errorPassword && password.isBlank()) "Campo obligatorio" else null,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Mostrar u ocultar contraseña"
                    )
                }
            }
        )

        Spacer(Modifier.height(28.dp))

        BotonPrimario(
            texto = "Iniciar Sesión",
            onClick = {
                val userTrim = usuario.trim()
                val passTrim = password.trim()

                var esValido = true
                if (userTrim.isEmpty()) {
                    errorUsuario = true
                    esValido = false
                }
                if (passTrim.isEmpty()) {
                    errorPassword = true
                    esValido = false
                }

                if (!esValido) {
                    mensajeErrorGlobal = "Por favor, complete todos los campos obligatorios."
                    return@BotonPrimario
                }

                // Validación de credenciales fijas
                val esUsuarioCorrecto = userTrim.equals(USUARIO_CORREO, ignoreCase = true) || userTrim == USUARIO_TELEFONO
                val esPassCorrecta = passTrim == PASSWORD_CORRECTA

                if (esUsuarioCorrecto && esPassCorrecta) {
                    mensajeErrorGlobal = null
                    onLoginExitoso()
                } else {
                    errorUsuario = true
                    errorPassword = true
                    mensajeErrorGlobal = "Credenciales incorrectas. Verifique su correo/teléfono y contraseña."
                }
            }
        )

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "¿No tienes una cuenta? ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Crear cuenta",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = AzulEnlace,
                modifier = Modifier.clickable(onClick = onIrARegistro)
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun EncabezadoLogin(onVolver: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = onVolver) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
        }
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f, fill = false)
        )
        Spacer(Modifier.size(48.dp))
    }
    Text(
        text = "Ingresa tus datos para acceder a tu cuenta",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PantallaLoginPreview() {
    BodegaTheme {
        PantallaLogin(onVolver = {}, onLoginExitoso = {}, onIrARegistro = {})
    }
}
