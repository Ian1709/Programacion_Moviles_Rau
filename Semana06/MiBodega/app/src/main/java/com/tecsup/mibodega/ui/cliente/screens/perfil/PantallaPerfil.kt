package com.tecsup.mibodega.ui.cliente.screens.perfil

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BarraNavegacionInferior
import com.tecsup.mibodega.ui.componentes.BarraSuperiorBodega
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

/**
 * Pantalla Perfil (Hito 8): Muestra la información personal y el Switch de Modo Oscuro.
 */
@Composable
fun PantallaPerfil(
    cantidadCarrito: Int,
    esModoOscuro: Boolean = false,
    onToggleModoOscuro: (Boolean) -> Unit = {},
    onVerCarrito: () -> Unit,
    destinoSeleccionado: Int = 3,
    onSeleccionarDestino: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            BarraSuperiorBodega(
                titulo = "Mi Perfil",
                cantidadCarrito = cantidadCarrito,
                onVerCarrito = onVerCarrito
            )
        },
        bottomBar = {
            BarraNavegacionInferior(
                destinoSeleccionado = destinoSeleccionado,
                onSeleccionarDestino = onSeleccionarDestino
            )
        }
    ) { paddingInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(GrisClaro, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Foto de perfil",
                    tint = VerdeBodega,
                    modifier = Modifier.size(80.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Cliente MiBodega",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Usuario Registrado",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(20.dp))

            // Card de Información Personal
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Información Personal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = VerdeBodega
                    )

                    Spacer(Modifier.height(12.dp))

                    ItemPerfilInfo(
                        icono = Icons.Default.Email,
                        titulo = "Correo Electrónico",
                        valor = "admin@mibodega.com"
                    )

                    Spacer(Modifier.height(12.dp))

                    ItemPerfilInfo(
                        icono = Icons.Default.Phone,
                        titulo = "Teléfono",
                        valor = "+51 987 654 321"
                    )

                    Spacer(Modifier.height(12.dp))

                    ItemPerfilInfo(
                        icono = Icons.Default.LocationOn,
                        titulo = "Dirección Principal",
                        valor = "Av. Los Olivos 123, Lima"
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Card de Configuración y Modo Oscuro
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Preferencias de la App",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = VerdeBodega
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DarkMode,
                            contentDescription = "Modo Oscuro",
                            tint = VerdeBodega,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Modo Oscuro",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Cambia el tema de colores de la app",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = esModoOscuro,
                            onCheckedChange = onToggleModoOscuro,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                                checkedTrackColor = VerdeBodega
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemPerfilInfo(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    titulo: String,
    valor: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icono,
            contentDescription = titulo,
            tint = VerdeBodega,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = titulo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = valor,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PantallaPerfilPreview() {
    BodegaTheme {
        PantallaPerfil(
            cantidadCarrito = 1,
            esModoOscuro = false,
            onToggleModoOscuro = {},
            onVerCarrito = {},
            onSeleccionarDestino = {}
        )
    }
}
