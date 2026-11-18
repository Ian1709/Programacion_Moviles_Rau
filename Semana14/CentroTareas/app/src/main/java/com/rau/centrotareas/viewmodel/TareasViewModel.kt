package com.rau.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TareasViewModel : ViewModel() {
    private val _toques = MutableStateFlow(0)
    val toques: StateFlow<Int> = _toques
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje

    fun tocar() { _toques.value++ }

    fun tareaLarga() {
        _mensaje.value = "Trabajando..."
        viewModelScope.launch {
            Log.d("HILO", "antes: ${Thread.currentThread().name}")
            withContext(Dispatchers.Default) {
                Log.d("HILO", "dentro: ${Thread.currentThread().name}")
                delay(4000)
            }
            Log.d("HILO", "después: ${Thread.currentThread().name}")
            _mensaje.value = "Terminó"
        }
    }
}
