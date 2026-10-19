package com.rau.cafecampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.rau.cafecampus.data.prefs.PreferenciasStore
import com.rau.cafecampus.data.repository.CafeRepositoryRoom
import com.rau.cafecampus.ui.navigation.NavCafe
import com.rau.cafecampus.ui.theme.CafeCampusTheme
import com.rau.cafecampus.viewmodel.CafeViewModelFactory
import com.rau.cafecampus.viewmodel.MenuViewModel
import com.rau.cafecampus.viewmodel.SesionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as CafeApp
        val repository = CafeRepositoryRoom(app.database)
        val preferenciasStore = PreferenciasStore(applicationContext)
        val factory = CafeViewModelFactory(repository, preferenciasStore)

        setContent {
            CafeCampusTheme {
                val navController = rememberNavController()
                val sesionViewModel: SesionViewModel = viewModel(factory = factory)
                val menuViewModel: MenuViewModel = viewModel(factory = factory)

                NavCafe(
                    navController = navController,
                    sesionViewModel = sesionViewModel,
                    menuViewModel = menuViewModel
                )
            }
        }
    }
}
