package com.tecsup.campusgps

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
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
                PantallaMapa(
                    vm = mapaViewModel,
                    onAbrirAjustes = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", packageName, null)
                        }
                        startActivity(intent)
                    }
                )
            }
        }
    }
}