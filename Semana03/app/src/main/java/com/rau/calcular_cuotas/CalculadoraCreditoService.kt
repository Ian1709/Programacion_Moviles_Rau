package com.rau.calcular_cuotas

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun imprimirCronograma(financiamiento: ResultadoFinanciamiento) {
        val cuotaFormateada = String.format("%.2f", financiamiento.cuotaMensual)
        var fechaPago = LocalDate.now().plusMonths(1)
        val formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        println("\n+-------------------------------------------------------------+")
        println("|               CRONOGRAMA DE PAGOS Y VENCIMIENTOS            |")
        println("+-------------------------------------------------------------+")
        for (i in 1..financiamiento.numCuotas) {
            val iStr = if (i < 10) "0$i" else "$i"
            val numStr = if (financiamiento.numCuotas < 10) "0${financiamiento.numCuotas}" else "${financiamiento.numCuotas}"
            val fechaStr = fechaPago.format(formatoFecha)

            println("|  Cuota $iStr/$numStr | Vence: $fechaStr | Monto: S/ $cuotaFormateada  |")
            fechaPago = fechaPago.plusMonths(1)
        }
        println("+-------------------------------------------------------------+")
    }
}