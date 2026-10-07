package com.rau.tecsupfit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Speed
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

data class Routine(
    val id: String,
    val title: String,
    val duration: String,
    val level: String,
    val exercisesCount: Int,
    val description: String,
    val icon: ImageVector,
)

@Composable
fun FitRoutinesScreen() {
    val sampleRoutines = listOf(
        Routine(
            id = "R1",
            title = "Rutina Fuerza Torso Superior",
            duration = "45 min",
            level = "Intermedio",
            exercisesCount = 6,
            description = "Press de banca, dominadas, remo con barra, press militar y flexiones.",
            icon = Icons.Default.FitnessCenter
        ),
        Routine(
            id = "R2",
            title = "Cardio HIIT Quema Grasa",
            duration = "30 min",
            level = "Avanzado",
            exercisesCount = 8,
            description = "Burpees, mountain climbers, saltos con cuerda y sprints de alta intensidad.",
            icon = Icons.AutoMirrored.Filled.DirectionsRun
        ),
        Routine(
            id = "R3",
            title = "Core & Abdomen de Acero",
            duration = "20 min",
            level = "Principiante",
            exercisesCount = 5,
            description = "Planchas frontales, giros rusos, elevación de piernas y crunches.",
            icon = Icons.Default.Speed
        ),
        Routine(
            id = "R4",
            title = "Estiramiento & Movilidad",
            duration = "25 min",
            level = "Todos los niveles",
            exercisesCount = 7,
            description = "Posturas de yoga, movilidad articular y estiramiento muscular profundo.",
            icon = Icons.Default.SelfImprovement
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftGreenBg)
    ) {
        // Banner Superior "Rutinas Sugeridas"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkGreen)
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Column {
                Text(
                    text = "Rutinas de Entrenamiento",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Planes diseñados para potenciar tus días libres y clases",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(sampleRoutines) { routine ->
                RoutineCard(routine = routine)
            }
        }
    }
}

@Composable
fun RoutineCard(
    routine: Routine,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkGreen.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = routine.icon,
                            contentDescription = routine.title,
                            tint = DarkGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = routine.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGreen
                        )
                        Text(
                            text = "${routine.exercisesCount} Ejercicios recomendados",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = routine.description,
                fontSize = 13.sp,
                color = Color.DarkGray,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Duración",
                        tint = MediumGreen,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = routine.duration,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkGreen.copy(alpha = 0.1f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = routine.level,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen
                    )
                }
            }
        }
    }
}
