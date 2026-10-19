package com.rau.cafecampus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rau.cafecampus.ui.admin.MenuAdminScreen
import com.rau.cafecampus.ui.admin.PanelScreen
import com.rau.cafecampus.ui.admin.PedidosAdminScreen
import com.rau.cafecampus.ui.cliente.ConfirmarPedidoScreen
import com.rau.cafecampus.ui.cliente.DetalleProductoScreen
import com.rau.cafecampus.ui.cliente.LoginScreen
import com.rau.cafecampus.ui.cliente.MenuScreen
import com.rau.cafecampus.ui.cliente.MisPedidosScreen
import com.rau.cafecampus.ui.cliente.SeguimientoScreen
import com.rau.cafecampus.ui.cliente.SplashScreen
import com.rau.cafecampus.viewmodel.MenuViewModel
import com.rau.cafecampus.viewmodel.PanelAdminViewModel
import com.rau.cafecampus.viewmodel.PedidosAdminViewModel
import com.rau.cafecampus.viewmodel.PedidosClienteViewModel
import com.rau.cafecampus.viewmodel.SesionViewModel

@Composable
fun NavCafe(
    navController: NavHostController,
    sesionViewModel: SesionViewModel,
    menuViewModel: MenuViewModel,
    pedidosViewModel: PedidosClienteViewModel,
    panelAdminViewModel: PanelAdminViewModel,
    pedidosAdminViewModel: PedidosAdminViewModel
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
                onIrAPedidos = {
                    navController.navigate(Rutas.MisPedidos.ruta)
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
            ConfirmarPedidoScreen(
                menuViewModel = menuViewModel,
                pedidosViewModel = pedidosViewModel,
                onVolverAlMenu = { navController.popBackStack() },
                onPedidoConfirmado = { pedidoId ->
                    navController.navigate(Rutas.Seguimiento.crearRuta(pedidoId)) {
                        popUpTo(Rutas.MenuCliente.ruta) { inclusive = false }
                    }
                }
            )
        }

        composable(
            route = Rutas.Seguimiento.ruta,
            arguments = listOf(navArgument("pedidoId") { type = NavType.LongType })
        ) { backStackEntry ->
            val pedidoId = backStackEntry.arguments?.getLong("pedidoId") ?: 0L
            SeguimientoScreen(
                pedidoId = pedidoId,
                pedidosViewModel = pedidosViewModel,
                onVolverAlMenu = {
                    navController.navigate(Rutas.MenuCliente.ruta) {
                        popUpTo(Rutas.MenuCliente.ruta) { inclusive = true }
                    }
                },
                onVerMisPedidos = {
                    navController.navigate(Rutas.MisPedidos.ruta) {
                        popUpTo(Rutas.MenuCliente.ruta) { inclusive = false }
                    }
                }
            )
        }

        composable(Rutas.MisPedidos.ruta) {
            MisPedidosScreen(
                pedidosViewModel = pedidosViewModel,
                onVolverAlMenu = { navController.popBackStack() },
                onVerSeguimiento = { pedidoId ->
                    navController.navigate(Rutas.Seguimiento.crearRuta(pedidoId))
                },
                onCerrarSesion = {
                    sesionViewModel.cerrarSesion()
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Rutas del Administrador
        composable(Rutas.MenuAdmin.ruta) {
            PanelScreen(
                panelViewModel = panelAdminViewModel,
                sesionViewModel = sesionViewModel,
                onVerPedidos = { navController.navigate(Rutas.PedidosAdmin.ruta) },
                onGestionarMenu = { navController.navigate(Rutas.PanelAdmin.ruta) },
                onAjustes = {
                    sesionViewModel.cerrarSesion()
                    navController.navigate(Rutas.Login.ruta) { popUpTo(0) { inclusive = true } }
                },
                onCerrarSesion = {
                    sesionViewModel.cerrarSesion()
                    navController.navigate(Rutas.Login.ruta) { popUpTo(0) { inclusive = true } }
                }
            )
        }

        composable(Rutas.PedidosAdmin.ruta) {
            PedidosAdminScreen(
                pedidosAdminViewModel = pedidosAdminViewModel,
                onVolver = { navController.popBackStack() },
                onGestionarMenu = { navController.navigate(Rutas.PanelAdmin.ruta) },
                onAjustes = {
                    sesionViewModel.cerrarSesion()
                    navController.navigate(Rutas.Login.ruta) { popUpTo(0) { inclusive = true } }
                },
                onDetallePedido = { }
            )
        }

        composable(Rutas.PanelAdmin.ruta) {
            MenuAdminScreen(
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
