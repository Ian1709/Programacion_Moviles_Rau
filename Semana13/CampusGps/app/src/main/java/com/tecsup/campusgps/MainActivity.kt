package com.tecsup.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.tecsup.campusgps.ui.PantallaMapa
import com.tecsup.campusgps.ui.theme.CampusGpsTheme
import com.tecsup.campusgps.viewmodel.CampusViewModelFactory
import com.tecsup.campusgps.viewmodel.MapaViewModel

class MainActivity : ComponentActivity() {
    private lateinit var mapaViewModel: MapaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mapaViewModel = ViewModelProvider(
            this,
            CampusViewModelFactory(applicationContext)
        )[MapaViewModel::class.java]

        setContent {
            CampusGpsTheme {
                PantallaMapa(vm = mapaViewModel)
            }
        }
    }
}