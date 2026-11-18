package com.tecsup.campusgps.data

import android.content.Context
import android.location.LocationManager
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.tecsup.campusgps.model.Posicion
import kotlinx.coroutines.tasks.await

class UbicacionRepositoryGoogle(context: Context) : UbicacionRepository {
    private val appContext = context.applicationContext
    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(appContext)

    override suspend fun obtenerUbicacion(): Posicion? {
        val location = fusedLocationProviderClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).await() ?: return null

        return Posicion(
            latitud = location.latitude,
            longitud = location.longitude
        )
    }

    override fun gpsDisponible(): Boolean {
        val locationManager =
            appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return try {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (_: IllegalArgumentException) {
            false
        }
    }
}