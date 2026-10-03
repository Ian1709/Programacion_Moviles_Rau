package com.tecsup.mibodega.ui.navegacion

/**
 * Definición centralizada de las rutas de navegación de la aplicación MiBodega.
 */
object Rutas {
    const val BIENVENIDA = "bienvenida"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val INICIO = "inicio"
    const val CATEGORIAS = "categorias"
    const val MIS_PEDIDOS = "mis_pedidos"
    const val PERFIL = "perfil"
    const val FAVORITOS = "favoritos"
    const val DETALLE = "detalle/{productoId}"
    const val CARRITO = "carrito"
    const val DATOS_ENTREGA = "datos_entrega"
    const val CONFIRMACION = "confirmacion"

    /**
     * Genera la ruta para la pantalla de detalle recibiendo el id del producto.
     */
    fun detalle(productoId: Int): String = "detalle/$productoId"
}
