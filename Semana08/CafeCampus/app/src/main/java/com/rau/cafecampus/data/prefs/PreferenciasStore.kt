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

    suspend fun guardaSesion(id: Int, nombre: String, correo: String, rol: String) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
            preferences[USER_ID] = id
            preferences[USER_NAME] = nombre
            preferences[USER_EMAIL] = correo
            preferences[USER_ROLE] = rol
        }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}