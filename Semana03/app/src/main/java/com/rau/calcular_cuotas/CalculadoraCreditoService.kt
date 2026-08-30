package com.rau.calcular_cuotas

data class ResultadoFinanciamiento(
    val numCuotas: Int,
    val porcentajeInteres: Double,
    val montoInteres: Double,
    val montoTotal: Double,
    val cuotaMensual: Double
)

class CalculadoraCreditoService {
    fun esCuotaValida(cuotas: Int): Boolean {
        return cuotas == 6 || cuotas == 12 || cuotas == 24
    }

    fun obtenerTasaInteres(cuotas: Int): Double {
        return when (cuotas) {
            6 -> 0.20
            12 -> 0.40
            24 -> 0.60
            else -> 0.0
        }
    }

    fun generarFinanciamiento(producto: Producto, cuotas: Int): ResultadoFinanciamiento {
        val tasa = obtenerTasaInteres(cuotas)
        val montoInicial = producto.montoInicial
        val montoInteres = montoInicial * tasa
        val montoTotal = montoInicial + montoInteres
        val cuotaMensual = montoTotal / cuotas

        return ResultadoFinanciamiento(
            numCuotas = cuotas,
            porcentajeInteres = tasa,
            montoInteres = montoInteres,
            montoTotal = montoTotal,
            cuotaMensual = cuotaMensual
        )
    }

    fun imprimirCronograma(financiamiento: ResultadoFinanciamiento) {
        println("\n--- CRONOGRAMA DE PAGOS ---")
        for (i in 1..financiamiento.numCuotas) {
            println("Cuota $i / ${financiamiento.numCuotas}: S/ ${"%.2f".format(financiamiento.cuotaMensual)}")
        }
        println("---------------------------")
    }
}
