package com.rau.tecsupstore.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.rau.tecsupstore.components.Producto
import com.rau.tecsupstore.components.TarjetaProducto
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var destinoActual by remember { mutableStateOf("Mis pedidos") }

    val productos = listOf(
        Producto("1", "Audifonos", 89.00),
        Producto("2", "Smartwatch", 199.00),
        Producto("3", "Funda celular", 25.00)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(destinoActual) { opcion ->
                destinoActual = opcion
                scope.launch { drawerState.close() }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("TECSUP Store") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, "Menú")
                        }
                    }
                )
            }
        ) { padding ->
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(productos) { producto ->
                    TarjetaProducto(producto)
                }
            }
        }
    }
}