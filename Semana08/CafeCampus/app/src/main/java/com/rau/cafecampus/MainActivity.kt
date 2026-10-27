package com.rau.cafecampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.rau.cafecampus.data.prefs.PreferenciasStore
import com.rau.cafecampus.data.repository.CafeRepositoryRoom
import com.rau.cafecampus.ui.navigation.NavCafe
import com.rau.cafecampus.ui.theme.CafeCampusTheme
import com.rau.cafecampus.viewmodel.CafeViewModelFactory
import com.rau.cafecampus.viewmodel.MenuAdminViewModel
import com.rau.cafecampus.viewmodel.MenuViewModel
import com.rau.cafecampus.viewmodel.PanelAdminViewModel
import com.rau.cafecampus.viewmodel.PedidosAdminViewModel
import com.rau.cafecampus.viewmodel.PedidosClienteViewModel
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
            val sesionViewModel: SesionViewModel = viewModel(factory = factory)
            val modoOscuroActivo by sesionViewModel.modoOscuro.collectAsState(initial = false)

            CafeCampusTheme(darkTheme = modoOscuroActivo) {
                val navController = rememberNavController()
                val menuViewModel: MenuViewModel = viewModel(factory = factory)
                val pedidosViewModel: PedidosClienteViewModel = viewModel(factory = factory)
                val panelAdminViewModel: PanelAdminViewModel = viewModel(factory = factory)
                val pedidosAdminViewModel: PedidosAdminViewModel = viewModel(factory = factory)
                val menuAdminViewModel: MenuAdminViewModel = viewModel(factory = factory)

                NavCafe(
                    navController = navController,
                    sesionViewModel = sesionViewModel,
                    menuViewModel = menuViewModel,
                    pedidosViewModel = pedidosViewModel,
                    panelAdminViewModel = panelAdminViewModel,
                    pedidosAdminViewModel = pedidosAdminViewModel,
                    menuAdminViewModel = menuAdminViewModel
                )
            }
        }
    }
}
