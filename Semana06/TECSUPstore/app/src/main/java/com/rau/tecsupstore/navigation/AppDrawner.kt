package com.rau.tecsupstore.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppDrawerContent(
    destinoActual: String,
    onSeleccionar: (String) -> Unit
) {
    val opciones = listOf("Inicio", "Mis pedidos", "Favoritos", "Perfil", "Cerrar sesion")

    ModalDrawerSheet {
        opciones.forEach { opcion ->
            NavigationDrawerItem(
                label = { Text(opcion) },
                icon = { Icon(Icons.Outlined.Circle, null) },
                selected = destinoActual == opcion,
                onClick = { onSeleccionar(opcion) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}