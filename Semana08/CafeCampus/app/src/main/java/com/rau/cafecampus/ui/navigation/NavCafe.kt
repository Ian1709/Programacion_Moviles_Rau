package com.rau.cafecampus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rau.cafecampus.ui.admin.AjustesScreen
import com.rau.cafecampus.ui.admin.DetallePedidoAdminScreen
import com.rau.cafecampus.ui.admin.FormularioProductoScreen
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
import com.rau.cafecampus.viewmodel.MenuAdminViewModel
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
    pedidosAdminViewModel: PedidosAdminViewModel,
    menuAdminViewModel: MenuAdminViewModel
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
                pedidosViewModel = pedidosViewModel,
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
                    navController.navigate(Rutas.Ajustes.ruta)
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
                menuViewModel = menuViewModel,
                onVolverAlMenu = { navController.popBackStack() },
                onVerSeguimiento = { pedidoId ->
                    navController.navigate(Rutas.Seguimiento.crearRuta(pedidoId))
                },
                onCerrarSesion = {
                    navController.navigate(Rutas.Ajustes.ruta)
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
                onAjustes = { navController.navigate(Rutas.Ajustes.ruta) },
                onCerrarSesion = {
                    sesionViewModel.cerrarSesion()
                    navController.navigate(Rutas.Login.ruta) { popUpTo(0) { inclusive = true } }
                }
            )
        }

        composable(Rutas.PedidosAdmin.ruta) {
            PedidosAdminScreen(
                pedidosAdminViewModel = pedidosAdminViewModel,
                sesionViewModel = sesionViewModel,
                onVolver = { navController.popBackStack() },
                onGestionarMenu = { navController.navigate(Rutas.PanelAdmin.ruta) },
                onAjustes = { navController.navigate(Rutas.Ajustes.ruta) },
                onDetallePedido = { pedidoId ->
                    navController.navigate(Rutas.DetallePedidoAdmin.crearRuta(pedidoId))
                }
            )
        }

        composable(
            route = Rutas.DetallePedidoAdmin.ruta,
            arguments = listOf(navArgument("pedidoId") { type = NavType.LongType })
        ) { backStackEntry ->
            val pedidoId = backStackEntry.arguments?.getLong("pedidoId") ?: 0L
            DetallePedidoAdminScreen(
                pedidoId = pedidoId,
                pedidosAdminViewModel = pedidosAdminViewModel,
                sesionViewModel = sesionViewModel,
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.PanelAdmin.ruta) {
            MenuAdminScreen(
                menuAdminViewModel = menuAdminViewModel,
                onVolver = { navController.popBackStack() },
                onVerPedidos = { navController.navigate(Rutas.PedidosAdmin.ruta) },
                onAjustes = { navController.navigate(Rutas.Ajustes.ruta) },
                onCrearProducto = {
                    navController.navigate(Rutas.FormularioProducto.crearRuta(null))
                },
                onEditarProducto = { prodId ->
                    navController.navigate(Rutas.FormularioProducto.crearRuta(prodId))
                }
            )
        }

        composable(
            route = Rutas.FormularioProducto.ruta,
            arguments = listOf(navArgument("productoId") {
                type = NavType.IntType
                defaultValue = 0
            })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
            FormularioProductoScreen(
                productoId = productoId,
                menuAdminViewModel = menuAdminViewModel,
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.Ajustes.ruta) {
            AjustesScreen(
                sesionViewModel = sesionViewModel,
                onVolver = { navController.popBackStack() },
                onIrAMenuCliente = {
                    navController.navigate(Rutas.MenuCliente.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onIrAMenuAdmin = {
                    navController.navigate(Rutas.MenuAdmin.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onCerrarSesion = {
                    sesionViewModel.cerrarSesion()
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
