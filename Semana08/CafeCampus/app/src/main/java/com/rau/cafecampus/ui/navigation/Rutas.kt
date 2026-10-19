package com.rau.cafecampus.ui.navigation

sealed class Rutas(val ruta: String) {
    object Splash : Rutas("splash")
    object Login : Rutas("login")
    object MenuCliente : Rutas("menu_cliente")
    object DetalleProducto : Rutas("detalle_producto/{productoId}") {
        fun crearRuta(productoId: Int) = "detalle_producto/$productoId"
    }
    object ConfirmarPedido : Rutas("confirmar_pedido")
    object Seguimiento : Rutas("seguimiento/{pedidoId}") {
        fun crearRuta(pedidoId: Long) = "seguimiento/$pedidoId"
    }
    object MisPedidos : Rutas("mis_pedidos")
    object MenuAdmin : Rutas("menu_admin")
    object FormularioProducto : Rutas("formulario_producto?productoId={productoId}") {
        fun crearRuta(productoId: Int? = null) = if (productoId != null) "formulario_producto?productoId=$productoId" else "formulario_producto"
    }
    object PedidosAdmin : Rutas("pedidos_admin")
    object DetallePedidoAdmin : Rutas("detalle_pedido_admin/{pedidoId}") {
        fun crearRuta(pedidoId: Long) = "detalle_pedido_admin/$pedidoId"
    }
    object PanelAdmin : Rutas("panel_admin")
    object Ajustes : Rutas("ajustes")
}
