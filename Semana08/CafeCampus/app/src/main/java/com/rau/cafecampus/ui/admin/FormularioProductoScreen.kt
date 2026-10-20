package com.rau.cafecampus.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rau.cafecampus.ui.components.CafeTextField
import com.rau.cafecampus.ui.theme.AzulAdmin
import com.rau.cafecampus.viewmodel.MenuAdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioProductoScreen(
    productoId: Int,
    menuAdminViewModel: MenuAdminViewModel,
    onVolver: () -> Unit
) {
    val uiState by menuAdminViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val categorias = listOf("BEBIDAS", "COMIDAS", "POSTRES")

    LaunchedEffect(productoId) {
        menuAdminViewModel.cargarProductoParaEdicion(productoId)
    }

    LaunchedEffect(uiState.mensajeError) {
        uiState.mensajeError?.let { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (productoId > 0) "Editar Producto" else "Nuevo Producto",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text("‹", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                CafeTextField(
                    value = uiState.nombre,
                    onValueChange = { menuAdminViewModel.onNombreChange(it) },
                    label = "Nombre del producto",
                    activeColor = AzulAdmin
                )

                CafeTextField(
                    value = uiState.descripcion,
                    onValueChange = { menuAdminViewModel.onDescripcionChange(it) },
                    label = "Descripción",
                    activeColor = AzulAdmin
                )

                CafeTextField(
                    value = uiState.precio,
                    onValueChange = { menuAdminViewModel.onPrecioChange(it) },
                    label = "Precio (S/.)",
                    activeColor = AzulAdmin,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                Column {
                    Text(text = "Categoría", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        categorias.forEach { cat ->
                            val selected = uiState.categoria.equals(cat, ignoreCase = true)
                            FilterChip(
                                selected = selected,
                                onClick = { menuAdminViewModel.onCategoriaChange(cat) },
                                label = { Text(cat) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AzulAdmin,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Disponible en el menú", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Switch(
                        checked = uiState.disponible,
                        onCheckedChange = { menuAdminViewModel.onDisponibleChange(it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = AzulAdmin)
                    )
                }
            }

            Button(
                onClick = {
                    menuAdminViewModel.guardarProducto(onExito = onVolver)
                },
                enabled = !uiState.estaCargando,
                colors = ButtonDefaults.buttonColors(containerColor = AzulAdmin),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Guardar Producto", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
