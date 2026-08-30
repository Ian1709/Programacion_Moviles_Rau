package com.rau.calcular_cuotas

fun main() {
    val producto = Producto(
        nombre = "Laptop Gaming ASUS",
        precioUnitario = 2500.0,
        cantidad = 2
    )

    val calculadora = CalculadoraCreditoService()
    val cuotasElegidas = 12 // Opciones validas: 6, 12 o 24

    println("=============================================")
    println("      SISTEMA DE CÁLCULO DE PAGOS A CRÉDITO  ")
    println("=============================================")
    println("Producto: ${producto.nombre}")
    println("Precio Unitario: S/. ${"%.2f".format(producto.precioUnitario)}")
    println("Cantidad: ${producto.cantidad}")
    println("Monto Inicial (Subtotal): S/. ${"%.2f".format(producto.montoInicial)}")

    if (calculadora.esCuotaValida(cuotasElegidas)) {
        val financiamiento = calculadora.generarFinanciamiento(producto, cuotasElegidas)

        println("Cuotas seleccionadas: ${financiamiento.numCuotas}")
        println("Tasa de interés aplicada: ${(financiamiento.porcentajeInteres * 100).toInt()}%")
        println("Monto de Interés: S/. ${"%.2f".format(financiamiento.montoInteres)}")
        println("Monto Total a Pagar: S/. ${"%.2f".format(financiamiento.montoTotal)}")
        println("Cuota Mensual: S/. ${"%.2f".format(financiamiento.cuotaMensual)}")

        calculadora.imprimirCronograma(financiamiento)
    } else {
        println("\n[ERROR]: Número de cuotas no permitido. Solo se aceptan 6, 12 o 24 cuotas.")
    }
}