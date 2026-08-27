package com.rau.semana02console

import java.util.Scanner

data class Producto(
    val nombre: String,
    val precio: Double,
    val cantidad: Int
)

fun main() {
    val scanner = Scanner(System.`in`)

    println("==========================================")
    println(" CARRITO DE COMPRAS - TIENDA TECSUP ")
    println("==========================================")

    print("Ingrese el nombre del cliente: ")
    val nombreCliente = scanner.nextLine()

    val carrito = mutableListOf<Producto>()

    print("\n¿Cuántos productos desea registrar en el carrito?: ")
    val cantidadProductos = scanner.nextInt()
    scanner.nextLine() // Limpieza de buffer

    for (i in 1..cantidadProductos) {
        println("\n--- Producto $i ---")
        print("Nombre del producto: ")
        val nombre = scanner.nextLine()

        print("Precio unitario (S/): ")
        val precio = scanner.nextDouble()
        scanner.nextLine()

        print("Cantidad: ")
        val cantidad = scanner.nextInt()
        scanner.nextLine()

        carrito.add(Producto(nombre, precio, cantidad))
    }

    println("\n--- RETO ADICIONAL: BÚSQUEDA ---")
    print("Ingrese el nombre del producto a buscar: ")
    val nombreBuscar = scanner.nextLine()
    val buscado = buscarProducto(carrito, nombreBuscar)

    if (buscado != null) {
        println("Encontrado: ${buscado.nombre} - Precio Unitario: S/ ${buscado.precio}")
    } else {
        println("Producto '$nombreBuscar' no encontrado.")
    }

    println("\n--- RETO ADICIONAL: ELIMINACIÓN ---")
    print("Ingrese el nombre del producto a eliminar: ")
    val nombreEliminar = scanner.nextLine()
    val eliminado = eliminarProducto(carrito, nombreEliminar)

    if (eliminado) {
        println("Producto '$nombreEliminar' eliminado exitosamente.")
    } else {
        println("No se encontró el producto a eliminar.")
    }

    println()
    mostrarDetalle(carrito)

    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val subtotalConIgv = subtotal + igv
    val descuento = calcularDescuento(subtotalConIgv)
    val totalFinal = subtotalConIgv - descuento

    println("Subtotal        : S/ ${String.format("%.2f", subtotal)}")
    println("IGV (18%)       : S/ ${String.format("%.2f", igv)}")
    if (descuento > 0.0) {
        println("Descuento Apl.  : S/ -${String.format("%.2f", descuento)}")
    }
    println("Total a Pagar   : S/ ${String.format("%.2f", totalFinal)}")
    println("==========================================")
    println("Gracias por su compra, $nombreCliente!")
}

fun mostrarDetalle(productos: List<Producto>) {
    println("---------------- DETALLE DEL CARRITO ----------------")
    var i = 1
    for (p in productos) {
        val importe = p.precio * p.cantidad
        println(String.format("%d. %-22s x%d  S/ %8.2f", i, p.nombre, p.cantidad, importe))
        i++
    }
    println("-----------------------------------------------------")
}

fun calcularSubtotal(productos: List<Producto>): Double {
    var subtotal = 0.0
    for (p in productos) {
        subtotal += p.precio * p.cantidad
    }
    return subtotal
}

fun calcularIGV(subtotal: Double): Double {
    return subtotal * 0.18
}

fun calcularTotal(subtotal: Double, igv: Double): Double {
    return subtotal + igv
}

fun calcularDescuento(total: Double): Double {
    return when {
        total > 5000 -> total * 0.10
        total > 3000 -> total * 0.05
        else -> 0.0
    }
}

fun buscarProducto(productos: List<Producto>, nombre: String): Producto? {
    return productos.find { it.nombre.equals(nombre, ignoreCase = true) }
}

fun eliminarProducto(productos: MutableList<Producto>, nombre: String): Boolean {
    return productos.removeIf { it.nombre.equals(nombre, ignoreCase = true) }
}