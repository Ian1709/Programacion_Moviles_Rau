package com.rau.cafecampus.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.rau.cafecampus.ui.theme.AzulAdmin
import com.rau.cafecampus.ui.theme.NaranjaEstudiante

enum class ClienteTab(val titulo: String, val icono: String) {
    MENU("Menú", "🏠"),
    PEDIDOS("Pedidos", "≡"),
    PERFIL("Perfil", "👤")
}

enum class AdminTab(val titulo: String, val icono: String) {
    PANEL("Panel", "📊"),
    PEDIDOS("Pedidos", "≡"),
    MENU("Menú", "☕"),
    AJUSTES("Ajustes", "⚙️")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    titulo: String,
    subtitulo: String? = null
) {
    TopAppBar(
        title = {
            Text(
                text = titulo,
                fontWeight = FontWeight.Bold
            )
        }
    )
}

@Composable
fun CafeBottomBar(
    tabActual: ClienteTab,
    onTabSeleccionada: (ClienteTab) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Gray
    ) {
        ClienteTab.entries.forEach { tab ->
            val seleccionada = tab == tabActual
            NavigationBarItem(
                selected = seleccionada,
                onClick = { onTabSeleccionada(tab) },
                icon = {
                    Text(
                        text = tab.icono,
                        fontSize = 18.sp
                    )
                },
                label = {
                    Text(
                        text = tab.titulo,
                        fontSize = 12.sp,
                        fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Normal,
                        color = if (seleccionada) NaranjaEstudiante else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NaranjaEstudiante,
                    selectedTextColor = NaranjaEstudiante,
                    indicatorColor = Color(0xFFFFF3E0)
                )
            )
        }
    }
}

@Composable
fun AdminBottomBar(
    tabActual: AdminTab,
    onTabSeleccionada: (AdminTab) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Gray
    ) {
        AdminTab.entries.forEach { tab ->
            val seleccionada = tab == tabActual
            NavigationBarItem(
                selected = seleccionada,
                onClick = { onTabSeleccionada(tab) },
                icon = {
                    Text(
                        text = tab.icono,
                        fontSize = 18.sp
                    )
                },
                label = {
                    Text(
                        text = tab.titulo,
                        fontSize = 12.sp,
                        fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Normal,
                        color = if (seleccionada) AzulAdmin else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AzulAdmin,
                    selectedTextColor = AzulAdmin,
                    indicatorColor = Color(0xFFEEEDFD)
                )
            )
        }
    }
}
