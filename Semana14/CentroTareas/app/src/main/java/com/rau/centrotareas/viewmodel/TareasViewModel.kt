package com.rau.centrotareas.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TareasViewModel : ViewModel() {
    private val _toques = MutableStateFlow(0)
    val toques: StateFlow<Int> = _toques
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje

    fun tocar() { _toques.value++ }

    fun tareaLargaMala() {
        _mensaje.value = "Trabajando..."
        Thread.sleep(4000)
        _mensaje.value = "Terminó"
    }
}
