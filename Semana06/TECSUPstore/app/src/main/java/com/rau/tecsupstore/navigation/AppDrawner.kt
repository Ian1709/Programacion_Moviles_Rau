package com.rau.tecsupstore.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class DrawerItem(val titulo: String)

@Composable
fun AppDrawer(
    destinoActual: String,
    contadorFavoritos: Int,
    onSeleccionar: (String) -> Unit
) {
    val opciones = listOf(
        DrawerItem("Inicio"),
        DrawerItem("Mis pedidos"),
        DrawerItem("Favoritos"),
        DrawerItem("Perfil"),
        DrawerItem("Cerrar sesion")
    )

    ModalDrawerSheet {
        Column(Modifier.padding(16.dp)) {
            Icon(Icons.Default.AccountCircle, null, Modifier.size(48.dp))
            Text("Ian Rau", style = MaterialTheme.typography.titleMedium)
            Text("ian.rau@tecsup.edu.pe", style = MaterialTheme.typography.bodySmall)
        }
        HorizontalDivider()
        opciones.forEach { item ->
            NavigationDrawerItem(
                label = { Text(item.titulo) },
                icon = { Icon(Icons.Outlined.Circle, null) },
                selected = destinoActual == item.titulo,
                onClick = { onSeleccionar(item.titulo) },
                badge = {
                    if (item.titulo == "Favoritos" && contadorFavoritos > 0) {
                        Badge { Text(contadorFavoritos.toString()) }
                    }
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}
