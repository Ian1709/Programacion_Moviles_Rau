package com.rau.tecsupfit.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rau.tecsupfit.model.MockFitData
import com.rau.tecsupfit.model.Reservation
import com.rau.tecsupfit.model.ReservationStatus
import com.rau.tecsupfit.ui.screens.FitClassDetailScreen
import com.rau.tecsupfit.ui.screens.FitConfirmationScreen
import com.rau.tecsupfit.ui.screens.FitHomeScreen
import com.rau.tecsupfit.ui.screens.FitProfileScreen
import com.rau.tecsupfit.ui.screens.FitReservationsScreen
import com.rau.tecsupfit.ui.screens.FitRoutinesScreen
import com.rau.tecsupfit.ui.theme.DarkGreen
import com.rau.tecsupfit.ui.theme.SoftGreenBg
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
)

@Composable
fun FitMainApp() {
    val navController = rememberNavController()

    // Gestión de estado reactivo en memoria exclusivamente con remember y mutableStateOf (sin ViewModel/MVVM)
    var classesState by remember { mutableStateOf(MockFitData.sampleClasses) }
    var reservationsState by remember { mutableStateOf(MockFitData.sampleReservations.toList()) }

    val navItems = listOf(
        BottomNavItem(
            route = FitScreen.Home.route,
            title = "Inicio",
            icon = Icons.Default.Home
        ),
        BottomNavItem(
            route = FitScreen.Reservations.route,
            title = "Reservas",
            icon = Icons.AutoMirrored.Filled.EventNote
        ),
        BottomNavItem(
            route = FitScreen.Routines.route,
            title = "Rutinas",
            icon = Icons.Default.FitnessCenter
        ),
        BottomNavItem(
            route = FitScreen.Profile.route,
            title = "Perfil",
            icon = Icons.Default.Person
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Ocultar NavigationBar en pantallas de detalle y confirmación
    val showBottomBar = navItems.any { it.route == currentRoute }

    Scaffold(
        containerColor = SoftGreenBg,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color.White,
                    contentColor = DarkGreen
                ) {
                    navItems.forEach { item ->
                        val selected = currentRoute == item.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = DarkGreen,
                                selectedTextColor = DarkGreen,
                                indicatorColor = DarkGreen.copy(alpha = 0.15f),
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = FitScreen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Pestaña Inicio
            composable(FitScreen.Home.route) {
                FitHomeScreen(
                    classesList = classesState,
                    onClassClick = { classId ->
                        navController.navigate(FitScreen.ClassDetail.createRoute(classId))
                    }
                )
            }

            // Pestaña Reservas
            composable(FitScreen.Reservations.route) {
                FitReservationsScreen(
                    reservationsList = reservationsState
                )
            }

            // Pestaña Rutinas
            composable(FitScreen.Routines.route) {
                FitRoutinesScreen()
            }

            // Pestaña Perfil
            composable(FitScreen.Profile.route) {
                FitProfileScreen()
            }

            // Pantalla Detalle de Clase
            composable(
                route = FitScreen.ClassDetail.route,
                arguments = listOf(
                    navArgument("classId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val classId = backStackEntry.arguments?.getString("classId") ?: ""
                FitClassDetailScreen(
                    classId = classId,
                    classesList = classesState,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onReserveClick = { selectedClassId, schedule ->
                        navController.navigate(FitScreen.Confirmation.createRoute(selectedClassId, schedule))
                    }
                )
            }

            // Pantalla Confirmación de Reserva
            composable(
                route = FitScreen.Confirmation.route,
                arguments = listOf(
                    navArgument("classId") { type = NavType.StringType },
                    navArgument("schedule") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val classId = backStackEntry.arguments?.getString("classId") ?: ""
                val rawSchedule = backStackEntry.arguments?.getString("schedule") ?: ""
                val schedule = URLDecoder.decode(rawSchedule, StandardCharsets.UTF_8.name())

                FitConfirmationScreen(
                    classId = classId,
                    schedule = schedule,
                    classesList = classesState,
                    onNavigateToReservations = {
                        // Actualizamos dinámicamente las reservas y reducimos cupos en el estado mutable en memoria
                        val targetClass = classesState.find { it.id == classId }
                        if (targetClass != null) {
                            // Reducir cupos
                            classesState = classesState.map { fitnessClass ->
                                if (fitnessClass.id == classId && fitnessClass.availableSpots > 0) {
                                    fitnessClass.copy(availableSpots = fitnessClass.availableSpots - 1)
                                } else {
                                    fitnessClass
                                }
                            }

                            // Añadir nueva reserva
                            val newReservation = Reservation(
                                id = "RES-${(102..999).random()}",
                                fitnessClass = targetClass,
                                selectedSchedule = schedule,
                                date = "Hoy (Reciente)",
                                status = ReservationStatus.CONFIRMED
                            )
                            reservationsState = listOf(newReservation) + reservationsState
                        }

                        navController.navigate(FitScreen.Reservations.route) {
                            popUpTo(FitScreen.Home.route)
                            launchSingleTop = true
                        }
                    },
                    onNavigateToHome = {
                        navController.navigate(FitScreen.Home.route) {
                            popUpTo(FitScreen.Home.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
