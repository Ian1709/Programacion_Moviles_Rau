package com.rau.campusgps.data

import com.rau.campusgps.model.Posicion

interface UbicacionRepository {
    suspend fun obtenerUbicacion(): Posicion?
    fun gpsDisponible(): Boolean
    fun playServicesDisponible(): Boolean
}