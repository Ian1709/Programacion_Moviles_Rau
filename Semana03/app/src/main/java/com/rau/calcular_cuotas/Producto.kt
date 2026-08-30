package com.rau.calcular_cuotas

data class Producto(
    val nombre: String,
    val precioUnitario: Double,
    val cantidad: Int
) {
    val montoInicial: Double
        get() = precioUnitario * cantidad
}

data class Financiamiento(
    val producto: Producto,
    val numCuotas: Int,
    val porcentajeInteres: Double
) {
    val montoInteres: Double
        get() = producto.montoInicial * porcentajeInteres

    val montoTotal: Double
        get() = producto.montoInicial + montoInteres

    val cuotaMensual: Double
        get() = montoTotal / numCuotas
}