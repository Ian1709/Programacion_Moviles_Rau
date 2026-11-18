package com.rau.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TareasViewModel : ViewModel() {
    private val _toques = MutableStateFlow(0)
    val toques: StateFlow<Int> = _toques
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje
    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso
    private val _corriendo = MutableStateFlow(false)
    val corriendo: StateFlow<Boolean> = _corriendo
    private var trabajo: Job? = null

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
}
