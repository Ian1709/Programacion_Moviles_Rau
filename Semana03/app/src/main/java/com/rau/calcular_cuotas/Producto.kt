package com.rau.calcular_cuotas

data class Producto(
    val nombre: String,
    val precioUnitario: Double,
    val cantidad: Int
) {
    val montoInicial: Double
        get() = precioUnitario * cantidad
}