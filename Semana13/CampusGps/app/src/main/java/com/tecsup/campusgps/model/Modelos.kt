package com.tecsup.campusgps.model

data class Posicion(
    val latitud: Double,
    val longitud: Double
)

data class Lugar(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val latitud: Double,
    val longitud: Double
)

data class LugarConDistancia(
    val lugar: Lugar,
    val metros: Float? = null
)

sealed interface UbicacionUi {
    data object SinBuscar : UbicacionUi
    data object Buscando : UbicacionUi
    data class Lista(val posicion: Posicion) : UbicacionUi
    data class Error(val mensaje: String) : UbicacionUi
}