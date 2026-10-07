package com.saludplus.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.saludplus.app.model.Appointment
import com.saludplus.app.model.MockData
import com.saludplus.app.ui.screens.BookAppointmentScreen
import com.saludplus.app.ui.screens.ConfirmationScreen
import com.saludplus.app.ui.screens.DoctorDetailScreen
import com.saludplus.app.ui.screens.HomeScreen
import com.saludplus.app.ui.screens.MedicalHistoryScreen
import com.saludplus.app.ui.screens.MyAppointmentsScreen
import kotlinx.coroutines.launch

private val PurplePrimary = Color(0xFF4A1E7A)

/**
 * Grafo de Navegación principal de Clínica Salud+.
 * Envuelve el NavHost dentro de un ModalNavigationDrawer de 3 destinos principales.
 * Mantiene la lista mutable de citas agendadas en memoria con remember y mutableStateOf.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Lista mutable de citas en memoria sin usar ViewModel ni MVVM
    var appointmentsList by remember { mutableStateOf(MockData.initialAppointments) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White
            ) {
                // Encabezado del Paciente en el Drawer Lateral
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PurplePrimary)
                        .padding(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Foto de perfil",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = MockData.patientName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    Text(
                        text = MockData.patientEmail,
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Item 1: Inicio / Médicos
                NavigationDrawerItem(
                    label = { Text("Inicio / Médicos") },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    selected = currentRoute == Screen.Home.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = PurplePrimary.copy(alpha = 0.1f),
                        selectedIconColor = PurplePrimary,
                        selectedTextColor = PurplePrimary
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                // Item 2: Mis Citas
                NavigationDrawerItem(
                    label = { Text("Mis Citas Médicas") },
                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                    selected = currentRoute == Screen.MyAppointments.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.MyAppointments.route) {
                            popUpTo(Screen.Home.route)
                        }
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = PurplePrimary.copy(alpha = 0.1f),
                        selectedIconColor = PurplePrimary,
                        selectedTextColor = PurplePrimary
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                // Item 3: Historial Médico
                NavigationDrawerItem(
                    label = { Text("Historial Médico") },
                    icon = { Icon(Icons.Default.History, contentDescription = null) },
                    selected = currentRoute == Screen.MedicalHistory.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.MedicalHistory.route) {
                            popUpTo(Screen.Home.route)
                        }
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = PurplePrimary.copy(alpha = 0.1f),
                        selectedIconColor = PurplePrimary,
                        selectedTextColor = PurplePrimary
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.fillMaxSize()
        ) {
            // 1. Pantalla Inicio / Catálogo de Médicos
            composable(route = Screen.Home.route) {
                HomeScreen(
                    onDoctorClick = { doctorId ->
                        navController.navigate(Screen.DoctorDetail.createRoute(doctorId))
                    },
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    }
                )
            }

            // 2. Detalle del Médico
            composable(
                route = Screen.DoctorDetail.route,
                arguments = listOf(
                    navArgument(Screen.DOCTOR_ID_ARG) { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val doctorId = backStackEntry.arguments?.getInt(Screen.DOCTOR_ID_ARG) ?: 1
                DoctorDetailScreen(
                    doctorId = doctorId,
                    onBackClick = { navController.popBackStack() },
                    onBookClick = { docId ->
                        navController.navigate(Screen.BookAppointment.createRoute(docId))
                    }
                )
            }

            // 3. Agendar Cita
            composable(
                route = Screen.BookAppointment.route,
                arguments = listOf(
                    navArgument(Screen.DOCTOR_ID_ARG) { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val doctorId = backStackEntry.arguments?.getInt(Screen.DOCTOR_ID_ARG) ?: 1
                BookAppointmentScreen(
                    doctorId = doctorId,
                    onBackClick = { navController.popBackStack() },
                    onConfirmBooking = { docId, selectedDate, selectedTime ->
                        val doctor = MockData.doctors.find { it.id == docId }
                        val newAppointmentId = (appointmentsList.maxOfOrNull { it.id } ?: 0) + 1
                        val newAppointment = Appointment(
                            id = newAppointmentId,
                            doctorId = docId,
                            doctorName = doctor?.name ?: "Médico Especialista",
                            doctorSpecialty = doctor?.specialty ?: "General",
                            doctorAvatar = doctor?.avatarUrl ?: "",
                            patientName = MockData.patientName,
                            date = selectedDate,
                            time = selectedTime,
                            status = "Confirmada",
                            reason = "Consulta Médica Agendada"
                        )
                        appointmentsList = listOf(newAppointment) + appointmentsList
                        navController.navigate(Screen.Confirmation.createRoute(newAppointmentId)) {
                            popUpTo(Screen.Home.route)
                        }
                    }
                )
            }

            // 4. Confirmación de Cita
            composable(
                route = Screen.Confirmation.route,
                arguments = listOf(
                    navArgument(Screen.APPOINTMENT_ID_ARG) { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val appointmentId = backStackEntry.arguments?.getInt(Screen.APPOINTMENT_ID_ARG) ?: 1
                ConfirmationScreen(
                    appointmentId = appointmentId,
                    appointments = appointmentsList,
                    onViewAppointmentsClick = {
                        navController.navigate(Screen.MyAppointments.route) {
                            popUpTo(Screen.Home.route)
                        }
                    },
                    onGoHomeClick = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }

            // 5. Mis Citas
            composable(route = Screen.MyAppointments.route) {
                MyAppointmentsScreen(
                    appointments = appointmentsList,
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    }
                )
            }

            // 6. Historial Médico
            composable(route = Screen.MedicalHistory.route) {
                MedicalHistoryScreen(
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    }
                )
            }
        }
    }
}
