package com.rau.cafecampus.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rau.cafecampus.ui.cliente.DetalleProductoScreen
import com.rau.cafecampus.ui.cliente.LoginScreen
import com.rau.cafecampus.ui.cliente.MenuScreen
import com.rau.cafecampus.ui.cliente.SplashScreen
import com.rau.cafecampus.viewmodel.MenuViewModel
import com.rau.cafecampus.viewmodel.SesionViewModel

@Composable
fun NavCafe(
    navController: NavHostController,
    sesionViewModel: SesionViewModel,
    menuViewModel: MenuViewModel
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
            MenuScreen(
                menuViewModel = menuViewModel,
                sesionViewModel = sesionViewModel,
                onProductoClick = { productoId ->
                    navController.navigate(Rutas.DetalleProducto.crearRuta(productoId))
                },
                onIrAConfirmarPedido = {
                    navController.navigate(Rutas.ConfirmarPedido.ruta)
                },
                onCerrarSesion = {
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(Rutas.MenuCliente.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Rutas.DetalleProducto.ruta,
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
            DetalleProductoScreen(
                productoId = productoId,
                menuViewModel = menuViewModel,
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.ConfirmarPedido.ruta) {
            Text("Pantalla Confirmar Pedido (Próximo commit)")
        }

        composable(Rutas.MenuAdmin.ruta) {
            Text("Pantalla de Menú Admin (En desarrollo)")
        }
    }
}
