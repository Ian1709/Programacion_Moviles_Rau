package com.tecsup.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.campusgps.data.LugaresRepository
import com.tecsup.campusgps.data.UbicacionRepository
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.model.Posicion
import com.tecsup.campusgps.model.UbicacionUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.tecsup.campusgps.util.distanciaMetros

class MapaViewModel(
    private val ubicacionRepository: UbicacionRepository,
    lugaresRepository: LugaresRepository
) : ViewModel() {
    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.SinBuscar)
    val estado = _estado.asStateFlow()

    private val _lugares = MutableStateFlow(
        lugaresRepository.obtenerLugares().map { LugarConDistancia(lugar = it) }
    )
    val lugares = _lugares.asStateFlow()

    private fun actualizarDistancias(posicion: Posicion) {
        _lugares.value = _lugares.value
            .map { lugarConDistancia ->
                lugarConDistancia.copy(
                    metros = distanciaMetros(posicion, lugarConDistancia.lugar)
                )
            }
            .sortedBy { it.metros }
    }
    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error("Permiso de ubicación denegado.")
    }

    fun buscar() {
        viewModelScope.launch {
            _estado.value = UbicacionUi.Buscando
            try {
                if (!ubicacionRepository.gpsDisponible()) {
                    _estado.value = UbicacionUi.Error("Activa el GPS e intenta de nuevo")
                    return@launch
                }

                val posicion = ubicacionRepository.obtenerUbicacion()
                _estado.value = if (posicion == null) {
                    UbicacionUi.Error("Activa el GPS e intenta de nuevo")
                } else {
                    UbicacionUi.Lista(posicion)
                }
            } catch (_: SecurityException) {
                permisoDenegado()
            } catch (_: Exception) {
                _estado.value = UbicacionUi.Error("Activa el GPS e intenta de nuevo")
            }
        }
    }
}