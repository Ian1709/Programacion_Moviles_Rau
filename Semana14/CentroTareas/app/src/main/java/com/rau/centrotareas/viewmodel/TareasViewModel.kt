package com.rau.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rau.centrotareas.data.TareasRepository
import com.rau.centrotareas.data.TareasRepositoryLocal
import com.rau.centrotareas.model.PrimosUi
import com.rau.centrotareas.model.CargaUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class TareasViewModel(private val repo: TareasRepository = TareasRepositoryLocal()) : ViewModel() {
    private val _toques = MutableStateFlow(0)
    val toques: StateFlow<Int> = _toques
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje
    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso
    private val _corriendo = MutableStateFlow(false)
    val corriendo: StateFlow<Boolean> = _corriendo
    private var trabajo: Job? = null
    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos: StateFlow<PrimosUi> = _primos
    private var trabajoPrimos: Job? = null
    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga: StateFlow<CargaUi> = _carga

    fun tocar() { _toques.value++ }

    fun tareaLarga() {
        if (_corriendo.value) return
        _progreso.value = 0f
        _mensaje.value = "Trabajando..."
        _corriendo.value = true
        trabajo = viewModelScope.launch {
            try {
            Log.d("HILO", "antes: ${Thread.currentThread().name}")
            withContext(Dispatchers.Default) {
                Log.d("HILO", "dentro: ${Thread.currentThread().name}")
                for (i in 1..100) {
                    delay(40)
                    _progreso.value = i / 100f
                }
            }
            Log.d("HILO", "después: ${Thread.currentThread().name}")
            _mensaje.value = "Terminó"
            } catch (e: CancellationException) {
                _mensaje.value = "Cancelado"
                throw e
            } finally {
                _corriendo.value = false
            }
        }
    }

    fun cancelar() {
        trabajo?.cancel()
    }

    fun contar(hasta: Int) {
        if (hasta < 2 || trabajoPrimos?.isActive == true) return
        trabajoPrimos = viewModelScope.launch {
            _primos.value = PrimosUi.Calculando
            val inicio = System.currentTimeMillis()
            try {
                val cantidad = repo.contarPrimos(hasta)
                _primos.value = PrimosUi.Listo(cantidad, System.currentTimeMillis() - inicio)
            } catch (e: CancellationException) {
                _primos.value = PrimosUi.Cancelado
                throw e
            }
        }
    }

    fun cancelarPrimos() { trabajoPrimos?.cancel() }

    fun cargarUnaPorUna() = cargar(paralelo = false)
    fun cargarALaVez() = cargar(paralelo = true)

    private fun cargar(paralelo: Boolean) {
        viewModelScope.launch {
            _carga.value = CargaUi.Cargando
            val inicio = System.currentTimeMillis()
            val (perfil, pedidos) = if (paralelo) {
                coroutineScope {
                    val perfilDeferred = async { repo.cargarPerfil() }
                    val pedidosDeferred = async { repo.cargarPedidos(false) }
                    perfilDeferred.await() to pedidosDeferred.await()
                }
            } else {
                val perfil = repo.cargarPerfil()
                perfil to repo.cargarPedidos(false)
            }
            _carga.value = CargaUi.Exito(if (paralelo) "A la vez" else "Una por una", perfil, pedidos.size, System.currentTimeMillis() - inicio)
        }
    }
}
