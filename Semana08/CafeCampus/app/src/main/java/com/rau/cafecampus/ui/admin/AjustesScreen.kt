package com.rau.cafecampus.ui.admin

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.rau.cafecampus.ui.components.AdminBottomBar
import com.rau.cafecampus.ui.components.AdminTab
import com.rau.cafecampus.ui.components.CafeBottomBar
import com.rau.cafecampus.ui.components.ClienteTab
import com.rau.cafecampus.ui.theme.AzulAdmin
import com.rau.cafecampus.ui.theme.NaranjaEstudiante
import com.rau.cafecampus.viewmodel.SesionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjustesScreen(
    sesionViewModel: SesionViewModel,
    onVolver: () -> Unit,
    onIrAMenuCliente: () -> Unit,
    onIrAMenuAdmin: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val nombre by sesionViewModel.userName.collectAsState(initial = "")
    val correo by sesionViewModel.userEmail.collectAsState(initial = "")
    val rol by sesionViewModel.userRole.collectAsState(initial = "CLIENTE")

    val modoOscuro by sesionViewModel.modoOscuro.collectAsState(initial = false)
    val notificaciones by sesionViewModel.notificaciones.collectAsState(initial = true)
    val recibirPedidos by sesionViewModel.recibirPedidos.collectAsState(initial = true)

    val fotoEstudiante by sesionViewModel.fotoPerfilEstudiante.collectAsState(initial = "")
    val fotoAdmin by sesionViewModel.fotoPerfilAdmin.collectAsState(initial = "")

    val esAdmin = rol.equals("ADMIN", ignoreCase = true)
    val colorAcento = if (esAdmin) AzulAdmin else NaranjaEstudiante
    val fondoBadge = MaterialTheme.colorScheme.surfaceVariant
    val fotoActual = if (esAdmin) fotoAdmin else fotoEstudiante

    // Launcher para seleccionar foto desde la galería del teléfono
    val launcherGaleria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            sesionViewModel.guardarFotoPerfil(it.toString(), esAdmin)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (esAdmin) "Ajustes" else "Mi Perfil",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text("‹", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            if (esAdmin) {
                AdminBottomBar(
                    tabActual = AdminTab.AJUSTES,
                    onTabSeleccionada = { tab ->
                        when (tab) {
                            AdminTab.PANEL -> onIrAMenuAdmin()
                            AdminTab.PEDIDOS -> onIrAMenuAdmin()
                            AdminTab.MENU -> onIrAMenuAdmin()
                            AdminTab.AJUSTES -> {}
                        }
                    }
                )
            } else {
                CafeBottomBar(
                    tabActual = ClienteTab.PERFIL,
                    onTabSeleccionada = { tab ->
                        when (tab) {
                            ClienteTab.MENU -> onIrAMenuCliente()
                            ClienteTab.PEDIDOS -> onIrAMenuCliente()
                            ClienteTab.PERFIL -> {}
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tarjeta de Perfil de Usuario con foto de galería personalizable
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = fondoBadge,
                            modifier = Modifier
                                .size(64.dp)
                                .clickable { launcherGaleria.launch("image/*") }
                        ) {
                            if (fotoActual.isNotBlank()) {
                                AsyncImage(
                                    model = fotoActual,
                                    contentDescription = "Foto de perfil",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("👤", fontSize = 30.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = if (nombre.isNotBlank()) nombre else if (esAdmin) "Administrador" else "Ian Rau",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (correo.isNotBlank()) correo else if (esAdmin) "admin@cafecampus.com" else "estudiante@cafecampus.com",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "📷 Cambiar foto de perfil",
                                color = colorAcento,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { launcherGaleria.launch("image/*") }
                            )
                        }
                    }
                }
            }

            // Botón Cambiar de Perfil
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val nuevoRol = if (esAdmin) "CLIENTE" else "ADMIN"
                            sesionViewModel.cambiarRolDemo(nuevoRol)
                            if (nuevoRol == "CLIENTE") onIrAMenuCliente() else onIrAMenuAdmin()
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colorAcento)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⇄", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Cambiar de perfil",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = if (esAdmin) "Modo práctica: ir a vista Estudiante" else "Modo práctica: ir a vista Administrador",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 12.sp
                                )
                            }
                        }
                        Text("›", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Sección: Preferencias
            item {
                Text(
                    text = "Preferencias",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Modo Oscuro
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Modo oscuro", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                                Text("Cambia el tema de la app", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                            }
                            Switch(
                                checked = modoOscuro,
                                onCheckedChange = { sesionViewModel.toggleModoOscuro(it) },
                                colors = SwitchDefaults.colors(checkedThumbColor = colorAcento)
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                        // Notificaciones
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Notificaciones", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                                Text(
                                    text = if (esAdmin) "Avisar nuevos pedidos" else "Avisar estado de mis pedidos",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp
                                )
                            }
                            Switch(
                                checked = notificaciones,
                                onCheckedChange = { sesionViewModel.toggleNotificaciones(it) },
                                colors = SwitchDefaults.colors(checkedThumbColor = colorAcento)
                            )
                        }

                        // Opción exclusiva para el Administrador
                        if (esAdmin) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Recibir pedidos", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                                    Text("Abrir o cerrar la cafetería", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                                }
                                Switch(
                                    checked = recibirPedidos,
                                    onCheckedChange = { sesionViewModel.toggleRecibirPedidos(it) },
                                    colors = SwitchDefaults.colors(checkedThumbColor = AzulAdmin)
                                )
                            }
                        }
                    }
                }
            }

            // Banner DataStore
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = fondoBadge)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Se guardan en el dispositivo", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                        Text("con DataStore", color = colorAcento, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            // Botón Cerrar sesión
            item {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {
                        sesionViewModel.cerrarSesion()
                        onCerrarSesion()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.Red),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Cerrar sesión", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
