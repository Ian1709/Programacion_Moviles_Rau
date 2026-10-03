package com.tecsup.mibodega.ui.cliente.screens.registro

import androidx.compose.runtime.Composable

/**
 * Delegado de la pantalla de Registro/Crear Cuenta para mantener compatibilidad.
 */
@Composable
fun RegistroScreen(
    onVolver: () -> Unit,
    onCrearCuenta: (nombre: String, telefono: String, direccion: String, referencia: String) -> Unit
) {
    PantallaCrearCuenta(
        onVolver = onVolver,
        onCrearCuenta = onCrearCuenta
    )
}
