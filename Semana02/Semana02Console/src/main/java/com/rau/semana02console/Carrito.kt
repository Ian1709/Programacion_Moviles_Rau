package com.rau.semana02console

data class Producto(
    val nombre: String,
    val precio: Double,
    val cantidad: Int
)

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

fun main() {
    println("=========================================")
    println(" CARRITO DE COMPRAS - TIENDA TECSUP ")
    println("=========================================")

    val nombreCliente = "Ian Rau" // String (inferido)
    val carrito = mutableListOf<Producto>() // lista vacía de productos
    println("Cliente: $nombreCliente\n")

    carrito.add(Producto("Laptop HP", 3500.0, 1))
    carrito.add(Producto("Mouse Logitech", 50.5, 2))
    carrito.add(Producto("Monitor Gamer 24", 650.0, 1))
    carrito.add(Producto("Camara Web Full HD", 145.0, 3))

    for (producto in carrito) {
        println("Producto agregado: ${producto.nombre}")
    }

    println()
    mostrarDetalle(carrito)
    println(String.format("%-25s : %d", "Cantidad de productos", carrito.size))

    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val total = calcularTotal(subtotal, igv)

    println(String.format("%-25s : S/ %8.2f", "Subtotal", subtotal))
    println(String.format("%-25s : S/ %8.2f", "IGV (18%%)", igv))
    println(String.format("%-25s : S/ %8.2f", "TOTAL A PAGAR", total))

    println()

    val masCaro = carrito.maxByOrNull { it.precio }
    if (masCaro != null) {
        println(String.format("Producto mas caro: %s (S/%.2f)", masCaro.nombre, masCaro.precio))
    }

    val descuento = calcularDescuento(total)
    if (descuento > 0.0) {
        val pct = if (total > 5000) "10%" else "5%"
        println("Descuento aplicado: $pct por compra mayor a S/ 3000")
        println(String.format("%-25s : S/ %8.2f", "TOTAL CON DESCUENTO", total - descuento))
    } else {
        println("Descuento aplicado: No aplica")
    }

    println("\nGracias por su compra, $nombreCliente!")

    println("\n--- RETO ADICIONAL ---")

    val buscado = buscarProducto(carrito, "Monitor Gamer 24")
    println("Buscado: ${buscado?.nombre} - S/ ${buscado?.precio}")

    eliminarProducto(carrito, "Camara Web Full HD")
    println("Producto eliminado. Carrito actual:")
    mostrarDetalle(carrito)
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
fun calcularDescuento(total: Double): Double {
    return when {
        total > 5000 -> total * 0.10
        total > 3000 -> total * 0.05
        else -> 0.0
    }
}
fun buscarProducto(productos: List<Producto>, nombre: String): Producto? {
    return productos.find { it.nombre == nombre }
}

fun eliminarProducto(productos: MutableList<Producto>, nombre: String) {
    productos.removeIf { it.nombre == nombre }
}