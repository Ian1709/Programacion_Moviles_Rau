package com.rau.cafecampus.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class PreferenciasStore(private val context: Context) {

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val USER_ID = intPreferencesKey("user_id")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_ROLE = stringPreferencesKey("user_role")

        private val MODO_OSCURO = booleanPreferencesKey("modo_oscuro")
        private val NOTIFICACIONES = booleanPreferencesKey("notificaciones")
        private val RECIBIR_PEDIDOS = booleanPreferencesKey("recibir_pedidos")
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    val userId: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[USER_ID] ?: -1
    }

    val userName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_NAME] ?: ""
    }

    val userEmail: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_EMAIL] ?: ""
    }

    val userRole: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_ROLE] ?: ""
    }

    val modoOscuro: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[MODO_OSCURO] ?: false
    }

    val notificaciones: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[NOTIFICACIONES] ?: true
    }

    val recibirPedidos: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[RECIBIR_PEDIDOS] ?: true
    }

    suspend fun guardaSesion(id: Int, nombre: String, correo: String, rol: String) {
        val nombreAMostrar = if (correo == "estudiante@cafecampus.com" || nombre.contains("Juan", ignoreCase = true)) "Ian Rau" else nombre
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
            preferences[USER_ID] = id
            preferences[USER_NAME] = nombreAMostrar
            preferences[USER_EMAIL] = correo
            preferences[USER_ROLE] = rol
        }
    }

    suspend fun setModoOscuro(activo: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[MODO_OSCURO] = activo
        }
    }

    suspend fun setNotificaciones(activo: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICACIONES] = activo
        }
    }

    suspend fun setRecibirPedidos(activo: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[RECIBIR_PEDIDOS] = activo
        }
    }

    suspend fun cambiarRol(nuevoRol: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ROLE] = nuevoRol
            if (nuevoRol == "ADMIN") {
                preferences[USER_ID] = 1
                preferences[USER_NAME] = "Administrador"
                preferences[USER_EMAIL] = "admin@cafecampus.com"
            } else {
                preferences[USER_ID] = 2
                preferences[USER_NAME] = "Ian Rau"
                preferences[USER_EMAIL] = "estudiante@cafecampus.com"
            }
        }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
