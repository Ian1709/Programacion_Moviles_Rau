package com.rau.clinicasalud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rau.clinicasalud.ui.theme.ClinicaSaludTheme
import com.saludplus.app.navigation.AppNavigation

/**
 * Actividad Principal de Clínica Salud+.
 * Inicia el flujo navegable mediante AppNavigation.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClinicaSaludTheme {
                AppNavigation()
            }
        }
    }
}
