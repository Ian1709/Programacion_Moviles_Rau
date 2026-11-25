package com.tecsup.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecsup.murocampus.data.PostRepositoryRemote
import com.tecsup.murocampus.data.remote.RetrofitClient
import com.tecsup.murocampus.ui.PantallaCrear
import com.tecsup.murocampus.ui.PantallaDetalle
import com.tecsup.murocampus.ui.PantallaLista
import com.tecsup.murocampus.ui.theme.MuroCampusTheme
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    private val vm: PostsViewModel by viewModels {
        PostsViewModelFactory(PostRepositoryRemote(RetrofitClient.api))
    }

    @OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(23, 105, 170)
        setContent {
            MuroCampusTheme {
                var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
                var creando by rememberSaveable { mutableStateOf(false) }
                BackHandler(enabled = idSeleccionado != null || creando) {
                    if (creando) creando = false else idSeleccionado = null
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("MuroCampus", color = Color.White) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color(0xFF1769AA),
                                titleContentColor = Color.White,
                            ),
                        )
                    },
                    floatingActionButton = {
                        if (idSeleccionado == null && !creando) {
                            FloatingActionButton(onClick = {
                                vm.limpiarCreacion()
                                creando = true
                            }) { Text("+") }
                        }
                    },
                ) { innerPadding: PaddingValues ->
                    when {
                        creando -> PantallaCrear(vm, Modifier.fillMaxSize().padding(innerPadding))
                        idSeleccionado != null -> PantallaDetalle(
                            vm = vm,
                            onReintentar = { vm.abrir(idSeleccionado!!) },
                            onVolver = { idSeleccionado = null },
                            modifier = Modifier.fillMaxSize().padding(innerPadding),
                        )
                        else -> PantallaLista(
                            vm = vm,
                            onAbrir = { id ->
                                idSeleccionado = id
                                vm.abrir(id)
                            },
                            modifier = Modifier.fillMaxSize().padding(innerPadding),
                        )
                    }
                }
            }
        }
    }
}
