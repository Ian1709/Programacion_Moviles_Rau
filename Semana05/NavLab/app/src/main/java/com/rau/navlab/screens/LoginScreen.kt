package com.rau.navlab.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rau.navlab.navigation.Screen

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF512DA8), // Morado intenso
            Color(0xFF7E57C2), // Morado intermedio
            Color(0xFFEDE7F6)  // Lavanda suave
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground),
        contentAlignment = Alignment.Center
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = Color.White // Forzado a blanco para evitar modo oscuro
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Título institucional
                Text(
                    text = "PORTAL ACADÉMICO",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    ),
                    color = Color(0xFF512DA8)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Inicia sesión con tu cuenta institucional",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF616161) // Gris oscuro para alto contraste
                )
                Spacer(modifier = Modifier.height(28.dp))

                // Campo de Correo Institucional / Usuario
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo institucional") },
                    placeholder = { Text("usuario@tecsup.edu.pe") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Ícono de correo",
                            tint = Color(0xFF512DA8)
                        )
                    },
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF512DA8),
                        focusedLabelColor = Color(0xFF512DA8),
                        unfocusedTextColor = Color(0xFF1A1A1A),
                        focusedTextColor = Color(0xFF1A1A1A)
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Campo de Contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    placeholder = { Text("••••••••") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Ícono de contraseña",
                            tint = Color(0xFF512DA8)
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF512DA8),
                        focusedLabelColor = Color(0xFF512DA8),
                        unfocusedTextColor = Color(0xFF1A1A1A),
                        focusedTextColor = Color(0xFF1A1A1A)
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Botón Principal INICIAR SESIÓN (Fondo morado sólido, texto blanco en mayúsculas)
                Button(
                    onClick = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF512DA8),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "INICIAR SESIÓN",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                // Botón Sutil para recuperación de credenciales
                TextButton(
                    onClick = { /* Acción de recuperación de contraseña */ }
                ) {
                    Text(
                        text = "¿Olvidaste tus credenciales?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF512DA8)
                    )
                }
            }
        }
    }
}
