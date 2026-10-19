package com.rau.cafecampus.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rau.cafecampus.ui.cliente.LoginScreen
import com.rau.cafecampus.ui.cliente.SplashScreen
import com.rau.cafecampus.viewmodel.SesionViewModel

@Composable
fun NavCafe(
    navController: NavHostController,
    sesionViewModel: SesionViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Rutas.Splash.ruta
    ) {
        composable(Rutas.Splash.ruta) {
            SplashScreen(
                sesionViewModel = sesionViewModel,
                onNavegarLogin = {
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(Rutas.Splash.ruta) { inclusive = true }
                    }
                },
                onNavegarCliente = {
                    navController.navigate(Rutas.MenuCliente.ruta) {
                        popUpTo(Rutas.Splash.ruta) { inclusive = true }
                    }
                },
                onNavegarAdmin = {
                    navController.navigate(Rutas.MenuAdmin.ruta) {
                        popUpTo(Rutas.Splash.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.Login.ruta) {
            LoginScreen(
                viewModel = sesionViewModel,
                onLoginExitosoCliente = {
                    navController.navigate(Rutas.MenuCliente.ruta) {
                        popUpTo(Rutas.Login.ruta) { inclusive = true }
                    }
                },
                onLoginExitosoAdmin = {
                    navController.navigate(Rutas.MenuAdmin.ruta) {
                        popUpTo(Rutas.Login.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.MenuCliente.ruta) {
            Text("Pantalla de Menú Cliente (En desarrollo)")
        }

        composable(Rutas.MenuAdmin.ruta) {
            Text("Pantalla de Menú Admin (En desarrollo)")
        }
    }
}
