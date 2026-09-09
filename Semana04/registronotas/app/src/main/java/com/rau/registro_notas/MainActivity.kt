package com.rau.registro_notas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rau.registro_notas.ui.theme.Registro_NotasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Registro_NotasTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        Surface(
                            color = Color(0xFF1A365D),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Registro de Notas",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                ),
                                modifier = Modifier
                                    .statusBarsPadding()
                                    .padding(horizontal = 20.dp, vertical = 16.dp)
                            )
                        }
                    }
                ) { innerPadding ->
                    PantallaNotas(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun PantallaNotas(modifier: Modifier = Modifier) {

    val fondoGradiente = Brush.verticalGradient(
        colors = listOf(Color(0xFFEBF8FF), Color(0xFFF7FAFC), Color.White)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(fondoGradiente)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {

    }
}