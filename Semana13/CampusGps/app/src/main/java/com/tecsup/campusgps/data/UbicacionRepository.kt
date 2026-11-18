package com.tecsup.campusgps.data

import com.tecsup.campusgps.model.Posicion

interface UbicacionRepository {
    suspend fun obtenerUbicacion(): Posicion?
    fun gpsDisponible(): Boolean
    fun playServicesDisponible(): Boolean
}