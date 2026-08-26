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
