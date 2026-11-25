package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(vm: PostsViewModel, onReintentar: () -> Unit, modifier: Modifier = Modifier) {
    val estado by vm.detalle.collectAsStateWithLifecycle()
    when (val actual = estado) {
        UiState.Cargando -> androidx.compose.foundation.layout.Box(
            modifier.fillMaxSize(), androidx.compose.ui.Alignment.Center,
        ) { Cargando("Cargando publicación...") }
        is UiState.Error -> androidx.compose.foundation.layout.Box(
            modifier.fillMaxSize().padding(16.dp), androidx.compose.ui.Alignment.Center,
        ) { ErrorConReintento(actual.mensaje, onReintentar) }
        is UiState.Exito -> {
            val detalle = actual.datos
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Usuario ${detalle.post.userId}", color = MaterialTheme.colorScheme.primary)
                        Text(detalle.post.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text(detalle.post.body, style = MaterialTheme.typography.bodyLarge)
                        Text("Comentarios (${detalle.comentarios.size})", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                }
                items(detalle.comentarios, key = { it.id }) { comentario ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    ) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(comentario.name, fontWeight = FontWeight.SemiBold)
                            Text(comentario.email, color = Color(0xFF1565C0))
                            Text(comentario.body, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
