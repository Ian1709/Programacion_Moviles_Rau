package com.tecsup.mibodega.ui.navegacion

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.Pedido
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.cliente.screens.bienvenida.BienvenidaScreen
import com.tecsup.mibodega.ui.cliente.screens.carrito.PantallaCarrito
import com.tecsup.mibodega.ui.cliente.screens.categorias.PantallaCategorias
import com.tecsup.mibodega.ui.cliente.screens.confirmacion.PantallaConfirmacion
import com.tecsup.mibodega.ui.cliente.screens.detalle.PantallaDetalleProducto
import com.tecsup.mibodega.ui.cliente.screens.entrega.PantallaDatosEntrega
import com.tecsup.mibodega.ui.cliente.screens.inicio.PantallaInicio
import com.tecsup.mibodega.ui.cliente.screens.login.PantallaLogin
import com.tecsup.mibodega.ui.cliente.screens.pedidos.PantallaMisPedidos
import com.tecsup.mibodega.ui.cliente.screens.perfil.PantallaPerfil
import com.tecsup.mibodega.ui.cliente.screens.registro.PantallaCrearCuenta
import com.tecsup.mibodega.ui.theme.BodegaTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Contenedor principal de navegación con soporte para Modo Oscuro,
 * gestión completa del Carrito e Historial de Pedidos confirmados.
 */
@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    var carrito by remember { mutableStateOf<List<ItemCarrito>>(emptyList()) }
    var favoritosIds by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var categoriaSeleccionadaGlobal by remember { mutableStateOf("Todos") }
    var esModoOscuro by remember { mutableStateOf(false) }

    var historialPedidos by remember { mutableStateOf<List<Pedido>>(emptyList()) }
    var ultimoPedidoConfirmado by remember { mutableStateOf<Pedido?>(null) }

    fun navegarABottomBar(destino: Int) {
        when (destino) {
            0 -> navController.navigate(Rutas.INICIO) { popUpTo(Rutas.INICIO) { inclusive = true } }
            1 -> navController.navigate(Rutas.CATEGORIAS) { popUpTo(Rutas.INICIO) }
            2 -> navController.navigate(Rutas.MIS_PEDIDOS) { popUpTo(Rutas.INICIO) }
            3 -> navController.navigate(Rutas.PERFIL) { popUpTo(Rutas.INICIO) }
        }
    }

    BodegaTheme(darkTheme = esModoOscuro) {
        NavHost(
            navController = navController,
            startDestination = Rutas.BIENVENIDA,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth / 3 },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth / 3 },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) {
            composable(Rutas.BIENVENIDA) {
                BienvenidaScreen(
                    onRegistrarse = { navController.navigate(Rutas.REGISTRO) },
                    onIniciarSesion = { navController.navigate(Rutas.LOGIN) },
                    onTerminos = { /* Abrir términos y condiciones */ }
                )
            }

            composable(Rutas.LOGIN) {
                PantallaLogin(
                    onVolver = { navController.popBackStack() },
                    onLoginExitoso = {
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.BIENVENIDA) { inclusive = true }
                        }
                    },
                    onIrARegistro = {
                        navController.navigate(Rutas.REGISTRO)
                    }
                )
            }

            composable(Rutas.REGISTRO) {
                PantallaCrearCuenta(
                    onVolver = { navController.popBackStack() },
                    onCrearCuenta = { _, _, _, _ ->
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.BIENVENIDA) { inclusive = true }
                        }
                    }
                )
            }

            composable(Rutas.INICIO) {
                PantallaInicio(
                    productos = listaProductosFake,
                    cantidadCarrito = carrito.sumOf { it.cantidad },
                    favoritosIds = favoritosIds,
                    categoriaInicial = categoriaSeleccionadaGlobal,
                    onToggleFavorito = { id ->
                        favoritosIds = if (favoritosIds.contains(id)) favoritosIds - id else favoritosIds + id
                    },
                    onVerCarrito = { navController.navigate(Rutas.CARRITO) },
                    onProductoClick = { producto ->
                        navController.navigate(Rutas.detalle(producto.id))
                    },
                    onAgregarProducto = { producto ->
                        carrito = agregarOSumarProducto(carrito, producto, 1)
                    },
                    destinoSeleccionado = 0,
                    onSeleccionarDestino = { navega -> navegarABottomBar(navega) }
                )
            }

            composable(Rutas.CATEGORIAS) {
                PantallaCategorias(
                    cantidadCarrito = carrito.sumOf { it.cantidad },
                    onVerCarrito = { navController.navigate(Rutas.CARRITO) },
                    onSeleccionarCategoria = { cat ->
                        categoriaSeleccionadaGlobal = cat
                        navController.navigate(Rutas.INICIO) { popUpTo(Rutas.INICIO) { inclusive = true } }
                    },
                    destinoSeleccionado = 1,
                    onSeleccionarDestino = { navega -> navegarABottomBar(navega) }
                )
            }

            composable(Rutas.MIS_PEDIDOS) {
                PantallaMisPedidos(
                    pedidos = historialPedidos,
                    cantidadCarrito = carrito.sumOf { it.cantidad },
                    onVerCarrito = { navController.navigate(Rutas.CARRITO) },
                    destinoSeleccionado = 2,
                    onSeleccionarDestino = { navega -> navegarABottomBar(navega) }
                )
            }

            composable(Rutas.PERFIL) {
                PantallaPerfil(
                    cantidadCarrito = carrito.sumOf { it.cantidad },
                    esModoOscuro = esModoOscuro,
                    onToggleModoOscuro = { esModoOscuro = it },
                    onVerCarrito = { navController.navigate(Rutas.CARRITO) },
                    destinoSeleccionado = 3,
                    onSeleccionarDestino = { navega -> navegarABottomBar(navega) }
                )
            }

            composable(
                route = Rutas.DETALLE,
                arguments = listOf(navArgument("productoId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
                val producto = listaProductosFake.firstOrNull { it.id == productoId } ?: listaProductosFake.first()

                PantallaDetalleProducto(
                    producto = producto,
                    esFavorito = favoritosIds.contains(producto.id),
                    onToggleFavorito = {
                        favoritosIds = if (favoritosIds.contains(producto.id)) favoritosIds - producto.id else favoritosIds + producto.id
                    },
                    onVolver = { navController.popBackStack() },
                    onAgregarAlCarrito = { productoSeleccionado, cantidad ->
                        carrito = agregarOSumarProducto(carrito, productoSeleccionado, cantidad)
                        navController.popBackStack()
                    }
                )
            }

            composable(Rutas.CARRITO) {
                PantallaCarrito(
                    carrito = carrito,
                    onVolver = { navController.popBackStack() },
                    onIncrementar = { producto ->
                        carrito = carrito.map {
                            if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + 1) else it
                        }
                    },
                    onDecrementar = { producto ->
                        carrito = carrito.mapNotNull {
                            when {
                                it.producto.id != producto.id -> it
                                it.cantidad > 1 -> it.copy(cantidad = it.cantidad - 1)
                                else -> null
                            }
                        }
                    },
                    onEliminar = { producto ->
                        carrito = carrito.filterNot { it.producto.id == producto.id }
                    },
                    onContinuarPedido = {
                        navController.navigate(Rutas.DATOS_ENTREGA)
                    }
                )
            }

            composable(Rutas.DATOS_ENTREGA) {
                val subtotal = carrito.sumOf { it.producto.precio * it.cantidad }
                val costoEnvio = 5.00
                val totalCalculado = subtotal + costoEnvio

                PantallaDatosEntrega(
                    total = totalCalculado,
                    onVolver = { navController.popBackStack() },
                    onConfirmarPedido = { direccion, referencia, telefono, nombreRecibe ->
                        val formatoFecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        val fechaActual = formatoFecha.format(Date())
                        val codigoPedido = "#PED-${1000 + historialPedidos.size + 1}"

                        val nuevoPedido = Pedido(
                            id = codigoPedido,
                            fecha = fechaActual,
                            items = carrito,
                            subtotal = subtotal,
                            costoEnvio = costoEnvio,
                            total = totalCalculado,
                            direccion = direccion,
                            referencia = referencia,
                            telefono = telefono,
                            nombreRecibe = nombreRecibe,
                            tipoEntrega = "Delivery a domicilio"
                        )

                        historialPedidos = listOf(nuevoPedido) + historialPedidos
                        ultimoPedidoConfirmado = nuevoPedido
                        carrito = emptyList()

                        navController.navigate(Rutas.CONFIRMACION) {
                            popUpTo(Rutas.INICIO)
                        }
                    }
                )
            }

            composable(Rutas.CONFIRMACION) {
                PantallaConfirmacion(
                    pedido = ultimoPedidoConfirmado,
                    onVerMisPedidos = {
                        navController.navigate(Rutas.MIS_PEDIDOS) {
                            popUpTo(Rutas.INICIO)
                        }
                    },
                    onVolverAlInicio = {
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.INICIO) { inclusive = true }
                        }
                    }
                )
            }

            composable(Rutas.FAVORITOS) {
                // Vista secundaria de favoritos si se requiere independientemente
            }
        }
    }
}

private fun agregarOSumarProducto(
    carrito: List<ItemCarrito>,
    producto: Producto,
    cantidad: Int
): List<ItemCarrito> {
    val itemExistente = carrito.find { it.producto.id == producto.id }
    return if (itemExistente != null) {
        carrito.map {
            if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + cantidad) else it
        }
    } else {
        carrito + ItemCarrito(producto = producto, cantidad = cantidad)
    }
}
