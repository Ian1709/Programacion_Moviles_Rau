package com.rau.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rau.campusgps.data.LugaresRepository
import com.rau.campusgps.data.UbicacionRepository
import com.rau.campusgps.model.Lugar
import com.rau.campusgps.model.LugarConDistancia
import com.rau.campusgps.model.Posicion
import com.rau.campusgps.model.UbicacionUi
import com.rau.campusgps.model.TipoError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.rau.campusgps.util.distanciaMetros

class MapaViewModel(
    private val ubicacionRepository: UbicacionRepository,
    lugaresRepository: LugaresRepository
) : ViewModel() {
    private val _modoOscuro = MutableStateFlow<Boolean?>(null)
    val modoOscuro = _modoOscuro.asStateFlow()

    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.SinBuscar)
    val estado = _estado.asStateFlow()

    private val _lugares = MutableStateFlow(
        lugaresRepository.obtenerLugares().map { LugarConDistancia(lugar = it) }
    )
    val lugares = _lugares.asStateFlow()

    private val _categoriaSeleccionada = MutableStateFlow<String?>(null)
    val categoriaSeleccionada = _categoriaSeleccionada.asStateFlow()

    val lugaresVisibles: StateFlow<List<LugarConDistancia>> = combine(
        _lugares,
        _categoriaSeleccionada
    ) { lista, categoria ->
        if (categoria == null) lista else lista.filter { it.lugar.categoria == categoria }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), _lugares.value)

    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado = _seleccionado.asStateFlow()

    private val _radioMetros = MutableStateFlow(100)
    val radioMetros = _radioMetros.asStateFlow()

    private val _masCercano: StateFlow<Lugar?> = lugaresVisibles
        .map { lista -> lista.firstOrNull { it.metros != null }?.lugar }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    val masCercano: StateFlow<Lugar?> = _masCercano

    private fun actualizarDistancias(posicion: Posicion) {
        _lugares.value = _lugares.value
            .map { lugarConDistancia ->
                val distancia = distanciaMetros(posicion, lugarConDistancia.lugar)
                lugarConDistancia.copy(
                    metros = distancia,
                    dentroDelRadio = distancia <= _radioMetros.value
                )
            }
            .sortedBy { it.metros }
    }
    fun cambiarModoOscuro(activado: Boolean) {
        _modoOscuro.value = activado
    }

    fun seleccionar(id: Int?) {
        _seleccionado.value = id
    }

    fun cambiarRadio(metros: Int) {
        if (metros in listOf(100, 200, 500)) {
            _radioMetros.value = metros
            val posicion = (_estado.value as? UbicacionUi.Lista)?.posicion
            if (posicion != null) actualizarDistancias(posicion)
        }
    }

    fun filtrarPorCategoria(categoria: String?) {
        _categoriaSeleccionada.value = categoria
        val lugarSeleccionado = _lugares.value.firstOrNull {
            it.lugar.id == _seleccionado.value
        }
        if (lugarSeleccionado != null && categoria != null &&
            lugarSeleccionado.lugar.categoria != categoria
        ) {
            _seleccionado.value = null
        }
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error("Permiso de ubicación denegado.", TipoError.SIN_PERMISO)
    }

    fun buscar() {
        viewModelScope.launch {
            _estado.value = UbicacionUi.Buscando
            try {
                if (!ubicacionRepository.playServicesDisponible()) {
                    _estado.value = UbicacionUi.Error(
                        "Este dispositivo no tiene Google Play Services.",
                        TipoError.SIN_PLAY_SERVICES
                    )
                    return@launch
                }

                if (!ubicacionRepository.gpsDisponible()) {
                    _estado.value = UbicacionUi.Error("Activa el GPS e intenta de nuevo", TipoError.GPS_APAGADO)
                    return@launch
                }

                val posicion = ubicacionRepository.obtenerUbicacion()
                _estado.value = if (posicion == null) {
                    UbicacionUi.Error("Activa el GPS e intenta de nuevo", TipoError.GPS_APAGADO)
                } else {
                    actualizarDistancias(posicion)
                    UbicacionUi.Lista(posicion)
                }
            } catch (_: SecurityException) {
                permisoDenegado()
            } catch (_: Exception) {
                _estado.value = UbicacionUi.Error(
                    "Ocurrió un error al obtener la ubicación.",
                    TipoError.OTRO
                )
            }
        }
    }
}
