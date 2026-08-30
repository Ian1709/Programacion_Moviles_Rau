package com.rau.calcular_cuotas

fun main() {
    println("=============================================")
    println("      SISTEMA DE CALCULO DE PAGOS A CREDITO  ")
    println("=============================================")

    print("Ingrese el nombre del producto: ")
    val nombre = readln()

    print("Ingrese el precio unitario (S/.): ")
    val precio = readln().toDoubleOrNull() ?: 0.0

    print("Ingrese la cantidad: ")
    val cantidad = readln().toIntOrNull() ?: 0

    print("Ingrese el numero de cuotas (6, 12 o 24): ")
    val cuotasElegidas = readln().toIntOrNull() ?: 0

    val calculadora = CalculadoraCreditoService()

    if (!calculadora.esCuotaValida(cuotasElegidas)) {
        println("\n[ERROR]: Entrada no valida. Solo se permiten 6, 12 o 24 cuotas.")
        return
    }

    val producto = Producto(nombre, precio, cantidad)
    val financiamiento = calculadora.generarFinanciamiento(producto, cuotasElegidas)

    println("\n=============================================")
    println("              RESUMEN DE VENTA               ")
    println("=============================================")
    println("Producto: ${producto.nombre}")
    println("Precio Unitario: S/ ${"%.2f".format(producto.precioUnitario)}")
    println("Cantidad: ${producto.cantidad}")
    println("Monto Inicial (Subtotal): S/ ${"%.2f".format(producto.montoInicial)}")

    println("\n--- RESULTADO FINANCIERO ---")
    println("Cuotas seleccionadas: ${financiamiento.numCuotas}")
    println("Interes aplicado: ${(financiamiento.porcentajeInteres * 100).toInt()}% (S/ ${"%.2f".format(financiamiento.montoInteres)})")
    println("Monto Total a Pagar: S/ ${"%.2f".format(financiamiento.montoTotal)}")
    println("Cuota Mensual: S/ ${"%.2f".format(financiamiento.cuotaMensual)}")

    calculadora.imprimirCronograma(financiamiento)
}