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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
 * Pantalla Perfil accesible desde la NavigationBar (BottomBar).
 */
@Composable
fun PantallaPerfil(
    cantidadCarrito: Int,
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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(12.dp))

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
                text = "Usuario registrado",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(24.dp))

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
                        titulo = "Correo",
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
                        titulo = "Dirección",
                        valor = "Av. Los Olivos 123, Lima"
                    )
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
            onVerCarrito = {},
            onSeleccionarDestino = {}
        )
    }
}
