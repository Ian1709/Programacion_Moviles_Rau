package com.rau.centrotareas.data

import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext

class TareasRepositoryLocal : TareasRepository {
    override suspend fun contarPrimos(hasta: Int): Int = withContext(Dispatchers.Default) {
        var cantidad = 0
        for (numero in 2..hasta) {
            currentCoroutineContext().ensureActive()
            if (esPrimo(numero)) cantidad++
        }
        cantidad
    }

    private fun esPrimo(numero: Int): Boolean {
        if (numero < 2) return false
        var divisor = 2
        while (divisor.toLong() * divisor <= numero) {
            if (numero % divisor == 0) return false
            divisor++
        }
        return true
    }

    override suspend fun cargarPerfil(): String {
        delay(2_000)
        return "Ana Torres (ficticia)"
    }

    override suspend fun cargarPedidos(simularError: Boolean): List<Int> {
        delay(3_000)
        if (simularError) throw IOException("Sin conexión")
        return listOf(101, 102, 103)
    }
}
