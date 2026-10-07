package com.rau.tecsupfit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.SportsMma
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rau.tecsupfit.model.FitnessClass
import com.rau.tecsupfit.model.MockFitData
import com.rau.tecsupfit.ui.theme.DarkGreen
import com.rau.tecsupfit.ui.theme.MediumGreen
import com.rau.tecsupfit.ui.theme.SoftGreenBg

@Composable
fun FitHomeScreen(
    classesList: List<FitnessClass> = MockFitData.sampleClasses,
    onClassClick: (String) -> Unit,
) {
    var selectedFilter by remember { mutableStateOf("Hoy") }

    val filteredClasses = remember(selectedFilter, classesList) {
        when (selectedFilter) {
            "Hoy" -> classesList.filter { it.isToday }
            "Esta semana" -> classesList
            else -> classesList
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftGreenBg)
    ) {
        // Banner Superior "TECSUP Fit" y saludo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkGreen)
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "TECSUP Fit",
                            color = Color.White,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "¡Hola, Estudiante! 👋",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 16.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "TF",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Reserva tu clase y mantén tu ritmo activo",
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // LazyRow con chips interactivos para filtrar
        Text(
            text = "Filtrar Clases",
            modifier = Modifier.padding(horizontal = 20.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGreen
        )

        Spacer(modifier = Modifier.height(8.dp))

        val filters = listOf("Hoy", "Esta semana", "Todas")
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters) { filter ->
                val isSelected = selectedFilter == filter
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedFilter = filter },
                    label = {
                        Text(
                            text = filter,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DarkGreen,
                        selectedLabelColor = Color.White,
                        containerColor = Color.White,
                        labelColor = DarkGreen
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Listado de clases en LazyColumn
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredClasses) { classItem ->
                FitnessClassCard(
                    fitnessClass = classItem,
                    onClick = { onClassClick(classItem.id) }
                )
            }
        }
    }
}

@Composable
fun FitnessClassCard(
    fitnessClass: FitnessClass,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkGreen.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getIconForClass(fitnessClass.iconName),
                            contentDescription = fitnessClass.title,
                            tint = DarkGreen,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = fitnessClass.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGreen
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = fitnessClass.instructor,
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MediumGreen.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = fitnessClass.category,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Horario",
                        tint = DarkGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = fitnessClass.schedules.firstOrNull() ?: "Consulte horarios",
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Sala",
                        tint = DarkGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = fitnessClass.room,
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cupos: ${fitnessClass.availableSpots} / ${fitnessClass.totalSpots}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (fitnessClass.availableSpots <= 3) Color(0xFFC62828) else MediumGreen
                )

                Text(
                    text = "Ver más >",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen
                )
            }
        }
    }
}

fun getIconForClass(iconName: String): ImageVector {
    return when (iconName) {
        "directions_bike" -> Icons.AutoMirrored.Filled.DirectionsBike
        "fitness_center" -> Icons.Default.FitnessCenter
        "self_improvement" -> Icons.Default.SelfImprovement
        "accessibility_new" -> Icons.Default.AccessibilityNew
        "sports_mma" -> Icons.Default.SportsMma
        else -> Icons.AutoMirrored.Filled.DirectionsRun
    }
}
