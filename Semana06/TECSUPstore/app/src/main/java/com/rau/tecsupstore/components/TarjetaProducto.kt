package com.rau.tecsupstore.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Producto(val id: String, val nombre: String, val precio: Double)

@Composable
fun TarjetaProducto(producto: Producto) {
    var expanded by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth().padding(8.dp, 4.dp)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.ShoppingBag, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text("S/ ${producto.precio}", color = MaterialTheme.colorScheme.secondary)
            }
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, "Opciones")
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(
                        text = { Text("Favoritos") },
                        onClick = { expanded = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Compartir") },
                        onClick = { expanded = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Reportar") },
                        onClick = { expanded = false }
                    )
                }
            }
        }
    }
}