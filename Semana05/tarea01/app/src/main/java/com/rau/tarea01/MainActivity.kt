package com.rau.tarea01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rau.tarea01.ui.theme.Tarea01Theme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tarea01Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        ContadorRoto()
                    }
                }
            }
        }
    }
}

@Composable
fun ContadorRoto() {
    var contador = 0 // Se resetea a 0 en cada recomposición
    Column {
        Text("Contador: $contador")
        Button(onClick = { contador++ }) { // No causa recomposición
            Text("Incrementar")
        }
    }
}

@Composable
fun ContadorConRemember() {
    // remember almacena el Int entre recomposiciones
    var contador by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Contador: $contador",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { contador++ }) {
            Text("Incrementar")
        }
    }
}
