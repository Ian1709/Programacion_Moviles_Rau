package com.rau.semana02console

// 1. ABSTRACCIÓN: Clase base con lo esencial de cualquier ítem
abstract class ElementoVenta(
    val nombre: String,
    precioBase: Double
) {
    // 4. ENCAPSULAMIENTO: Validación de datos de entrada
    var precioBase: Double = precioBase
        protected set(value) {
            require(value >= 0.0) { "El precio no puede ser negativo" }
            field = value
        }

    init {
        this.precioBase = precioBase
    }

    // Metodo abstracto que cada hijo debe implementar (Polimorfismo)
    abstract fun calcularPrecioFinal(): Double
}

// 2. HERENCIA: Producto hereda de ElementoVenta
class Producto(
    nombre: String,
    precio: Double,
    cantidad: Int
) : ElementoVenta(nombre, precio) {

    // 4. ENCAPSULAMIENTO: Atributo privado con getter/setter controlado
    var cantidad: Int = cantidad
        set(value) {
            require(value > 0) { "La cantidad debe ser mayor a 0" }
            field = value
        }

    init {
        this.cantidad = cantidad
    }

    // 3. POLIMORFISMO: Implementación propia del cálculo de precio
    override fun calcularPrecioFinal(): Double {
        return precioBase * cantidad
    }
}

// --- FUNCIONES DE LÓGICA DEL CARRITO ---

fun calcularSubtotal(productos: List<Producto>): Double {
    var subtotal = 0.0
    for (p in productos) {
        subtotal += p.calcularPrecioFinal() // Uso de Polimorfismo
    }
    return subtotal
}

fun calcularIGV(subtotal: Double): Double {
    return subtotal * 0.18
}

fun calcularTotal(subtotal: Double, igv: Double): Double {
    return subtotal + igv
}

fun buscarProducto(productos: List<Producto>, nombre: String): Producto? {
    return productos.find { it.nombre.equals(nombre, ignoreCase = true) }
}

fun eliminarProducto(productos: MutableList<Producto>, nombre: String): Boolean {
    return productos.removeIf { it.nombre.equals(nombre, ignoreCase = true) }
}

// --- PUNTO DE ENTRADA MAIN ---

fun main() {
    println("==========================================")
    println(" CARRITO DE COMPRAS - TIENDA TECSUP ")
    println("==========================================")

    val nombreCliente = "Ian Rau"
    val carrito = mutableListOf<Producto>()
    println("Cliente: $nombreCliente\n")

    // Agregar productos iniciales
    carrito.add(Producto("Laptop HP", 3500.0, 1))
    carrito.add(Producto("Mouse Logitech", 50.5, 2))
    carrito.add(Producto("Teclado Redragon", 150.0, 1))
    carrito.add(Producto("Monitor Gamer 24", 650.0, 1))

    // Reto Adicional: Búsqueda y Eliminación
    println("--- RETO ADICIONAL ---")
    val buscado = buscarProducto(carrito, "Monitor Gamer 24")
    if (buscado != null) {
        println("Buscado: ${buscado.nombre} - S/ ${buscado.precioBase}")
    } else {
        println("Producto no encontrado.")
    }

    eliminarProducto(carrito, "Teclado Redragon")
    println("Producto eliminado. Carrito actual:")

    // Visualización del detalle
    println("---------------- DETALLE DEL CARRITO ----------------")
    for ((index, prod) in carrito.withIndex()) {
        val totalProd = prod.calcularPrecioFinal()
        val linea = "${index + 1}. ${prod.nombre}".padEnd(25) +
                "x${prod.cantidad}".padEnd(6) +
                "S/ " + String.format("%.2f", totalProd).padStart(8)
        println(linea)
    }
    println("-----------------------------------------------------")

    // Cálculos de montos
    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val total = calcularTotal(subtotal, igv)

    println("Subtotal : S/ ${String.format("%.2f", subtotal)}")
    println("IGV (18%): S/ ${String.format("%.2f", igv)}")
    println("Total    : S/ ${String.format("%.2f", total)}")
    println("==========================================")
    println("Gracias por su compra, $nombreCliente!")
}
