package com.rau.centrotareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.rau.centrotareas.ui.PantallaTareas
import com.rau.centrotareas.ui.theme.CentroTareasTheme
import com.rau.centrotareas.viewmodel.TareasViewModel
import com.rau.centrotareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {
    private val viewModel by lazy { TareasViewModelFactory().create(TareasViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CentroTareasTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    PantallaTareas(viewModel)
                }
            }
        }
    }
}
