package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.model.Post
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(vm: PostsViewModel, onAbrir: (Int) -> Unit, modifier: Modifier = Modifier) {
    val estado by vm.lista.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()

    Column(modifier.fillMaxSize()) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                FilterChip(
                    selected = filtro == null,
                    onClick = { vm.filtrar(null) },
                    label = { Text("Todos") },
                )
            }
            items((1..10).toList()) { userId ->
                FilterChip(
                    selected = filtro == userId,
                    onClick = { vm.filtrar(userId) },
                    label = { Text("Usuario $userId") },
                )
            }
        }

        when (val actual = estado) {
            UiState.Cargando -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Cargando()
            }
            is UiState.Error -> Box(Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                ErrorConReintento(actual.mensaje, vm::cargar)
            }
            is UiState.Exito -> {
                if (actual.datos.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Sin publicaciones", style = MaterialTheme.typography.titleMedium)
                    }
                } else {
                    ListaTarjetas(actual.datos, onAbrir)
                }
            }
        }
    }
}

@Composable
private fun ListaTarjetas(posts: List<Post>, onAbrir: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(posts, key = { it.id }) { post ->
            TarjetaPost(post = post, onClick = { onAbrir(post.id) })
        }
    }
}
