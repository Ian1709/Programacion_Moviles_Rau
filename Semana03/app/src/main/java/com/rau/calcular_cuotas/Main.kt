package com.rau.calcular_cuotas

import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)

    println("+--------------------------------------------------+")
    println("|      SISTEMA DE CALCULO DE PAGOS A CREDITO       |")
    println("+--------------------------------------------------+")

    print("  >> Ingrese el nombre del producto   : ")
    val nombre = scanner.nextLine()

    var precio = 0.0
    while (precio <= 0) {
        print("  >> Ingrese el precio unitario (S/)  : ")
        if (scanner.hasNextDouble()) {
            precio = scanner.nextDouble()
            if (precio <= 0) println("     [!] El precio debe ser mayor a 0.")
        } else {
            println("     [!] Entradas inválidas. Ingrese un número.")
            scanner.next()
        }
    }

    var cantidad = 0
    while (cantidad <= 0) {
        print("  >> Ingrese la cantidad             : ")
        if (scanner.hasNextInt()) {
            cantidad = scanner.nextInt()
            if (cantidad <= 0) println("     [!] La cantidad debe ser mayor a 0.")
        } else {
            println("     [!] Entradas inválidas. Ingrese un número entero.")
            scanner.next()
        }
    }

    print("  >> Ingrese el nro. cuotas (6/12/24): ")
    val cuotasElegidas = if (scanner.hasNextInt()) scanner.nextInt() else 0

    val calculadora = CalculadoraCreditoService()

    if (!calculadora.esCuotaValida(cuotasElegidas)) {
        println("\n+--------------------------------------------------+")
        println("| [ERROR]: Solo se permiten plazos de 6, 12 o 24.  |")
        println("+--------------------------------------------------+")
        return
    }

    val producto = Producto(nombre, precio, cantidad)
    val financiamiento = calculadora.generarFinanciamiento(producto, cuotasElegidas)

    val interesPorcentaje = (financiamiento.porcentajeInteres * 100).toInt()
    val montoInicialStr = String.format("%.2f", producto.montoInicial)
    val precioUnitarioStr = String.format("%.2f", producto.precioUnitario)
    val montoInteresStr = String.format("%.2f", financiamiento.montoInteres)
    val montoTotalStr = String.format("%.2f", financiamiento.montoTotal)
    val cuotaMensualStr = String.format("%.2f", financiamiento.cuotaMensual)

    println("\n====================================================")
    println("                  RESUMEN DE VENTA                  ")
    println("====================================================")
    println("  Producto                 : ${producto.nombre}")
    println("  Precio Unitario          : S/ $precioUnitarioStr")
    println("  Cantidad                 : ${producto.cantidad}")
    println("  Monto Inicial (Subtotal) : S/ $montoInicialStr")

    println("\n----------------------------------------------------")
    println("                RESULTADO FINANCIERO                ")
    println("----------------------------------------------------")
    println("  Cuotas seleccionadas     : ${financiamiento.numCuotas} meses")
    println("  Interes aplicado         : $interesPorcentaje% (S/ $montoInteresStr)")
    println("  Monto Total a Pagar      : S/ $montoTotalStr")
    println("  Cuota Mensual            : S/ $cuotaMensualStr")

    calculadora.imprimirCronograma(financiamiento)
}