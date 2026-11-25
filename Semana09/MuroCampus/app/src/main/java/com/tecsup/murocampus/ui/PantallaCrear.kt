package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@Composable
fun PantallaCrear(vm: PostsViewModel, modifier: Modifier = Modifier) {
    var titulo by rememberSaveable { mutableStateOf("") }
    var contenido by rememberSaveable { mutableStateOf("") }
    val resultado by vm.creacion.collectAsStateWithLifecycle()

    LaunchedEffect(resultado) {
        if (resultado is UiState.Exito) {
            titulo = ""
            contenido = ""
        }
    }

    Column(
        modifier = modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Nueva publicación", style = androidx.compose.material3.MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        OutlinedTextField(
            value = contenido,
            onValueChange = { contenido = it },
            label = { Text("Contenido") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 5,
        )
        Button(
            onClick = { vm.publicar(titulo, contenido) },
            enabled = titulo.isNotBlank() && contenido.isNotBlank() && resultado !is UiState.Cargando,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Publicar")
        }
        when (val estado = resultado) {
            UiState.Cargando -> CircularProgressIndicator()
            is UiState.Error -> ErrorConReintento(estado.mensaje) { vm.publicar(titulo, contenido) }
            is UiState.Exito -> Text("Publicado con id ${estado.datos.id}")
            null -> Unit
        }
    }
}
