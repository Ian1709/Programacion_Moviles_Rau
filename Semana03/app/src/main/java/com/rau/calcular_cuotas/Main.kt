package com.rau.calcular_cuotas

fun main() {
    println("+--------------------------------------------------+")
    println("|      SISTEMA DE CALCULO DE PAGOS A CREDITO       |")
    println("+--------------------------------------------------+")

    print("  >> Ingrese el nombre del producto   : ")
    val nombre = readln()

    print("  >> Ingrese el precio unitario (S/)  : ")
    val precio = readln().toDoubleOrNull() ?: 0.0

    print("  >> Ingrese la cantidad             : ")
    val cantidad = readln().toIntOrNull() ?: 0

    print("  >> Ingrese el nro. cuotas (6/12/24): ")
    val cuotasElegidas = readln().toIntOrNull() ?: 0

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