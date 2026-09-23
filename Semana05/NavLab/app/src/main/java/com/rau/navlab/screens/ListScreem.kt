package com.rau.navlab.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rau.navlab.navigation.Screen

data class Student(
    val id: Int,
    val name: String,
    val career: String,
    val code: String,
    val email: String,
    val bio: String
)

val mockStudents = listOf(
    Student(1, "Carlos Mendoza", "Ingeniería de Software", "20241011", "carlos.mendoza@tecsup.edu.pe", "Apasionado por el desarrollo backend y arquitecturas limpias en Kotlin y Spring Boot."),
    Student(2, "Ana Sofía Vargas", "Redes y Comunicaciones", "20241012", "ana.vargas@tecsup.edu.pe", "Especialista en seguridad de redes, ciberseguridad ofensiva y administración de servidores Linux."),
    Student(3, "Luis Fernando Quispe", "Ciberseguridad", "20241013", "luis.quispe@tecsup.edu.pe", "Investigador de vulnerabilidades y analista de operaciones de seguridad SOC."),
    Student(4, "María Fernanda Torres", "Inteligencia Artificial", "20241014", "maria.torres@tecsup.edu.pe", "Desarrolladora de modelos de Machine Learning y procesamiento de lenguaje natural con Python."),
    Student(5, "Jorge Alberto Ruiz", "Ciencia de Datos", "20241015", "jorge.ruiz@tecsup.edu.pe", "Analista de grandes volúmenes de datos, visualización avanzada y estadística aplicada."),
    Student(6, "Claudia Patricia Ramos", "Diseño y Desarrollo de Software", "20241016", "claudia.ramos@tecsup.edu.pe", "Diseñadora UI/UX y desarrolladora frontend especializada en experiencias móviles con Jetpack Compose."),
    Student(7, "Diego Alejandro Rojas", "Desarrollo Móvil", "20241017", "diego.rojas@tecsup.edu.pe", "Entusiasta de Android nativo, arquitectura MVVM y concurrencia con Kotlin Coroutines y Flow."),
    Student(8, "Valeria Nicole Benites", "Cloud Computing", "20241018", "valeria.benites@tecsup.edu.pe", "Arquitecta cloud junior certificada en despliegues automatizados con Docker y Kubernetes.")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavController) {
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF512DA8),
            Color(0xFF7E57C2),
            Color(0xFFEDE7F6)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Directorio de Alumnos",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF512DA8)
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(mockStudents) { student ->
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    Screen.Detail.createRoute(student.id)
                                )
                            },
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEDE7F6)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Alumno",
                                    tint = Color(0xFF512DA8)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = student.name,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color(0xFF212121)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = student.career,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = Color(0xFF512DA8)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
