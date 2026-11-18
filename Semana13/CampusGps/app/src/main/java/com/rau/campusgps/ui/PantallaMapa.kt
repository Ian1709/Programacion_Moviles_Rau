package com.rau.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.rau.campusgps.model.UbicacionUi
import com.rau.campusgps.ui.theme.*
import com.rau.campusgps.util.formatearDistancia
import com.rau.campusgps.viewmodel.MapaViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel, onAbrirAjustes: () -> Unit) {
    val context = LocalContext.current
    val estado by vm.estado.collectAsState()
    val lugares by vm.lugaresVisibles.collectAsState()
    val categoriaSeleccionada by vm.categoriaSeleccionada.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()
    val radioMetros by vm.radioMetros.collectAsState()

    val permisoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permisos ->
        val concedido = permisos[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permisos[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (concedido) vm.buscar() else vm.permisoDenegado()
    }
    val tienePermiso = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    val solicitarUbicacion = {
        if (tienePermiso) vm.buscar() else permisoLauncher.launch(
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("CampusGPS", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("Tu campus, más cerca", style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = 0.76f))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CampusNavy, titleContentColor = Color.White)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp, vertical = 9.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val error = estado as? UbicacionUi.Error
            if (error != null) {
                Spacer(Modifier.height(8.dp))
                Text("UBICACIÓN", color = CampusBlue, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                Text("Revisa el acceso a tu ubicación", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                BotonUbicacion(habilitado = true, onClick = solicitarUbicacion)
                MensajeError(error, onAbrirAjustes, vm::buscar)
                Spacer(Modifier.weight(1f))
            } else {
                val categorias = listOf("Estudio", "Comida", "Eventos", "Servicios")
                Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp, shadowElevation = 2.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 8.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterChip(categoriaSeleccionada == null, { vm.filtrarPorCategoria(null) }, label = { Text("Todos") }, colors = chipColors())
                        categorias.forEach { categoria ->
                            FilterChip(categoriaSeleccionada == categoria, { vm.filtrarPorCategoria(categoria) }, label = { Text(categoria) }, colors = chipColors())
                        }
                    }
                }

                Box(
                    modifier = Modifier.weight(1.2f).fillMaxWidth()
                        .clip(RoundedCornerShape(25.dp))
                        .border(1.dp, CampusOutline, RoundedCornerShape(25.dp))
                ) {
                    MapaUbicacion(
                        estado = estado, lugares = lugares, seleccionado = seleccionado,
                        masCercanoId = masCercano?.id, radioMetros = radioMetros,
                        modifier = Modifier.fillMaxSize()
                    )
                    Surface(
                        modifier = Modifier.align(Alignment.TopStart).padding(12.dp),
                        shape = CircleShape, color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f), tonalElevation = 3.dp
                    ) {
                        Text(
                            if (estado is UbicacionUi.Lista) "CAMPUS · LIMA" else "MAPA DEL CAMPUS",
                            modifier = Modifier.padding(horizontal = 13.dp, vertical = 8.dp),
                            color = CampusInk, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold
                        )
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Radio", color = CampusMuted, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        listOf(100, 200, 500).forEach { radio ->
                            FilterChip(radioMetros == radio, { vm.cambiarRadio(radio) }, label = { Text("${radio} m") }, colors = chipColors())
                        }
                    }
                }

                BotonUbicacion(estado !is UbicacionUi.Buscando, solicitarUbicacion)
                EstadoUbicacion(estado)

                if (estado is UbicacionUi.Lista) {
                    val distancia = lugares.firstOrNull { it.lugar.id == masCercano?.id }?.metros
                    Surface(
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(Modifier.size(32.dp).clip(CircleShape).background(CampusGreen), contentAlignment = Alignment.Center) {
                                Text("★", color = Color.White, style = MaterialTheme.typography.labelLarge)
                            }
                            Column(Modifier.weight(1f)) {
                                Text("MÁS CERCANO", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                                Text(
                                    if (masCercano != null && distancia != null) "${masCercano!!.nombre} · ${formatearDistancia(distancia)}" else "Sin lugares en esta categoría",
                                    style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                            OutlinedButton(
                                onClick = { vm.seleccionar(masCercano?.id) }, enabled = masCercano != null,
                                shape = RoundedCornerShape(13.dp), contentPadding = PaddingValues(horizontal = 9.dp, vertical = 6.dp)
                            ) { Text("Ir al más cercano", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold) }
                        }
                    }

                    Row(Modifier.fillMaxWidth().padding(top = 1.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                        Column {
                            Text("Lugares cercanos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("${lugares.size} puntos para explorar", style = MaterialTheme.typography.bodySmall, color = CampusMuted)
                        }
                        if (seleccionado != null) Text("Seleccionado", style = MaterialTheme.typography.labelMedium, color = CampusBlue, fontWeight = FontWeight.SemiBold)
                    }
                    ListaLugares(
                        lugares = lugares, seleccionado = seleccionado, onClick = vm::seleccionar,
                        masCercanoId = masCercano?.id,
                        modifier = Modifier.fillMaxWidth().weight(0.82f)
                    )
                }
            }
        }
    }
}

@Composable
private fun EstadoUbicacion(estado: UbicacionUi) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(17.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f)) {
        when (estado) {
            UbicacionUi.SinBuscar -> Text("Pulsa el botón para empezar", Modifier.padding(horizontal = 14.dp, vertical = 9.dp), color = CampusMuted, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
            UbicacionUi.Buscando -> Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                Text("  Buscando tu ubicación…", color = CampusMuted, style = MaterialTheme.typography.bodySmall)
            }
            is UbicacionUi.Lista -> Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 7.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                Coordenada("LATITUD", "%.4f".format(Locale.US, estado.posicion.latitud))
                Coordenada("LONGITUD", "%.4f".format(Locale.US, estado.posicion.longitud))
            }
            is UbicacionUi.Error -> Unit
        }
    }
}

@Composable
private fun Coordenada(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = CampusMuted, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun chipColors() = FilterChipDefaults.filterChipColors(
    selectedContainerColor = CampusBlue, selectedLabelColor = Color.White,
    containerColor = MaterialTheme.colorScheme.surfaceVariant, labelColor = CampusMuted
)

@Composable
private fun BotonUbicacion(habilitado: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick, enabled = habilitado, shape = RoundedCornerShape(17.dp),
        colors = ButtonDefaults.buttonColors(containerColor = CampusBlue, contentColor = Color.White),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp, pressedElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().height(49.dp)
    ) {
        if (habilitado) {
            Row(horizontalArrangement = Arrangement.spacedBy(9.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(9.dp).clip(CircleShape).background(Color.White))
                Text("Obtener mi ubicación", fontWeight = FontWeight.Bold)
            }
        } else CircularProgressIndicator(Modifier.size(21.dp), color = Color.White, strokeWidth = 2.dp)
    }
}
