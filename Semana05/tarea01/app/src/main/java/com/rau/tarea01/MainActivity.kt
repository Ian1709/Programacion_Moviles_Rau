package com.rau.tarea01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import com.rau.tarea01.ui.theme.Tarea01Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tarea01Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        ContadorRoto()
                        Spacer(modifier = Modifier.height(32.dp))
                        TemperatureDisplay()
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

@Composable
fun TemperatureDisplay() {
    var temperatura by remember { mutableStateOf(20) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Temperatura: $temperatura°C",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { temperatura++ }) {
                Text("Subir")
            }
            Button(onClick = { temperatura-- }) {
                Text("Bajar")
            }
            OutlinedButton(onClick = { temperatura = 20 }) {
                Text("Resetear")
            }
        }
    }
}
