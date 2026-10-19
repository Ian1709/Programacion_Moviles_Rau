package com.rau.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rau.cafecampus.data.prefs.PreferenciasStore
import com.rau.cafecampus.data.repository.CafeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SesionUiState(
    val correo: String = "",
    val clave: String = "",
    val estaCargando: Boolean = false,
    val mensajeError: String? = null,
    val esLoginExitoso: Boolean = false,
    val rolUsuario: String = ""
)

class SesionViewModel(
    private val repository: CafeRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(SesionUiState())
    val uiState: StateFlow<SesionUiState> = _uiState.asStateFlow()

    val isLoggedIn = preferenciasStore.isLoggedIn
    val userRole = preferenciasStore.userRole

    fun onCorreoChange(nuevoCorreo: String) {
        _uiState.value = _uiState.value.copy(correo = nuevoCorreo, mensajeError = null)
    }

    fun onClaveChange(nuevaClave: String) {
        _uiState.value = _uiState.value.copy(clave = nuevaClave, mensajeError = null)
    }

    fun seleccionarRol(rol: String) {
        if (rol == "ADMIN") {
            _uiState.value = _uiState.value.copy(
                correo = "admin@cafecampus.com",
                clave = "admin123",
                mensajeError = null
            )
        } else {
            _uiState.value = _uiState.value.copy(
                correo = "estudiante@cafecampus.com",
                clave = "123456",
                mensajeError = null
            )
        }
    }

    fun iniciarSesion() {
        val correo = _uiState.value.correo.trim()
        val clave = _uiState.value.clave.trim()

        if (correo.isEmpty() || clave.isEmpty()) {
            _uiState.value = _uiState.value.copy(mensajeError = "Ingrese correo y contraseña")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(estaCargando = true, mensajeError = null)
            val usuario = repository.login(correo, clave)

            if (usuario != null) {
                preferenciasStore.guardaSesion(
                    id = usuario.id,
                    nombre = usuario.nombre,
                    correo = usuario.correo,
                    rol = usuario.rol
                )
                _uiState.value = _uiState.value.copy(
                    estaCargando = false,
                    esLoginExitoso = true,
                    rolUsuario = usuario.rol
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    estaCargando = false,
                    mensajeError = "Credenciales incorrectas"
                )
            }
        }
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            preferenciasStore.cerrarSesion()
            _uiState.value = SesionUiState()
        }
    }

    fun limpiarEstado() {
        _uiState.value = _uiState.value.copy(esLoginExitoso = false, mensajeError = null)
    }
}
