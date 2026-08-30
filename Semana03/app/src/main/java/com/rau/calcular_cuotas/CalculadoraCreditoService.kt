package com.rau.calcular_cuotas

class CalculadoraCreditoService {

    fun esCuotaValida(cuotas: Int): Boolean {
        return cuotas in listOf(6, 12, 24)
    }

    fun obtenerPorcentajeInteres(cuotas: Int): Double {
        return when (cuotas) {
            6 -> 0.20
            12 -> 0.40
            24 -> 0.60
            else -> 0.0
        }
    }

    fun generarFinanciamiento(producto: Producto, cuotas: Int): Financiamiento {
        val interes = obtenerPorcentajeInteres(cuotas)
        return Financiamiento(producto, cuotas, interes)
    }
}