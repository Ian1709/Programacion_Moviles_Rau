package com.rau.cafecampus.ui.cliente

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rau.cafecampus.ui.components.CafeTextField
import com.rau.cafecampus.ui.theme.AzulAdmin
import com.rau.cafecampus.ui.theme.FondoSegmentoGris
import com.rau.cafecampus.ui.theme.FondoTarjetaAdmin
import com.rau.cafecampus.ui.theme.NaranjaEstudiante
import com.rau.cafecampus.viewmodel.SesionViewModel

@Composable
fun LoginScreen(
    viewModel: SesionViewModel,
    onLoginExitosoCliente: () -> Unit,
    onLoginExitosoAdmin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Determinar rol activo (Cliente por defecto o Admin)
    val esAdmin = uiState.correo == "admin@cafecampus.com"
    val colorActivo by animateColorAsState(
        targetValue = if (esAdmin) AzulAdmin else NaranjaEstudiante,
        label = "colorActivo"
    )

    LaunchedEffect(uiState.esLoginExitoso) {
        if (uiState.esLoginExitoso) {
            val rol = uiState.rolUsuario
            viewModel.limpiarEstado()
            if (rol == "ADMIN") {
                onLoginExitosoAdmin()
            } else {
                onLoginExitosoCliente()
            }
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Encabezado
                Text(
                    text = "Bienvenido",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Ingresa para continuar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Selector de Rol Segmentado
                Surface(
                    color = FondoSegmentoGris,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Pestaña Estudiante
                        Surface(
                            color = if (!esAdmin) NaranjaEstudiante else Color.Transparent,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .clickable { viewModel.seleccionarRol("CLIENTE") }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "Estudiante",
                                    color = if (!esAdmin) Color.White else Color.Gray,
                                    fontWeight = if (!esAdmin) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        // Pestaña Administrador
                        Surface(
                            color = if (esAdmin) AzulAdmin else Color.Transparent,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .clickable { viewModel.seleccionarRol("ADMIN") }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "Administrador",
                                    color = if (esAdmin) Color.White else Color.Gray,
                                    fontWeight = if (esAdmin) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Campos de entrada
                CafeTextField(
                    value = uiState.correo,
                    onValueChange = { viewModel.onCorreoChange(it) },
                    label = "Correo",
                    activeColor = colorActivo,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = uiState.mensajeError != null,
                    errorMessage = uiState.mensajeError
                )

                Spacer(modifier = Modifier.height(16.dp))

                CafeTextField(
                    value = uiState.clave,
                    onValueChange = { viewModel.onClaveChange(it) },
                    label = "Contraseña",
                    activeColor = colorActivo,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = uiState.mensajeError != null
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Enlace "¿Olvidaste tu contraseña?"
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    TextButton(onClick = { }) {
                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            color = colorActivo,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón Ingresar
                Button(
                    onClick = { viewModel.iniciarSesion() },
                    colors = ButtonDefaults.buttonColors(containerColor = colorActivo),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !uiState.estaCargando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    if (uiState.estaCargando) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.height(24.dp)
                        )
                    } else {
                        Text(
                            text = "Ingresar",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Sección inferior según Rol
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!esAdmin) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "¿No tienes cuenta? ",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Regístrate",
                            color = NaranjaEstudiante,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.clickable { }
                        )
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = FondoTarjetaAdmin),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Modo práctica",
                                color = AzulAdmin,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Puedes cambiar de perfil desde Ajustes para probar ambos roles.",
                                color = Color(0xFF49454F),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
