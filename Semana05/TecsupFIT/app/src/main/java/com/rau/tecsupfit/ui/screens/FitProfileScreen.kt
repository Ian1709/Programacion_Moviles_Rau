package com.rau.tecsupfit.ui.screens

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rau.tecsupfit.ui.theme.DarkGreen
import com.rau.tecsupfit.ui.theme.MediumGreen
import com.rau.tecsupfit.ui.theme.SoftGreenBg

@Composable
fun FitProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftGreenBg)
            .verticalScroll(rememberScrollState())
    ) {
        // Banner Superior con Avatar e información del usuario
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkGreen)
                .padding(horizontal = 20.dp, vertical = 28.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Avatar circular con iniciales
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "AF",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Alex Flores",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Plan Estudiante Premium TECSUP Fit",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }

        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Sección de Métricas del Usuario
            Text(
                text = "Tus Estadísticas",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreen
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "Clases",
                    value = "12",
                    unit = "completadas",
                    icon = Icons.Default.FitnessCenter,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Racha",
                    value = "5 días",
                    unit = "activos",
                    icon = Icons.Default.LocalFireDepartment,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Horas",
                    value = "18 hrs",
                    unit = "entrenadas",
                    icon = Icons.Default.Timer,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Configuración del perfil
            Text(
                text = "Ajustes de Cuenta",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreen
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    ProfileOptionItem(
                        icon = Icons.Default.Person,
                        title = "Información Personal",
                        subtitle = "Actualiza tus datos académicos y personales"
                    )
                    ProfileOptionItem(
                        icon = Icons.Default.CardMembership,
                        title = "Membresía & Suscripción",
                        subtitle = "Estado activo hasta Diciembre 2026"
                    )
                    ProfileOptionItem(
                        icon = Icons.Default.Notifications,
                        title = "Notificaciones de Clases",
                        subtitle = "Recordatorios de turnos reservados"
                    )
                    ProfileOptionItem(
                        icon = Icons.Default.Settings,
                        title = "Preferencias del Sistema",
                        subtitle = "Configuración de interfaz y privacidad"
                    )
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    unit: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = DarkGreen,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreen
            )
            Text(
                text = unit,
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ProfileOptionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(DarkGreen.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = DarkGreen,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C1C1C)
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "Ir",
            tint = Color.LightGray,
            modifier = Modifier.size(14.dp)
        )
    }
}
