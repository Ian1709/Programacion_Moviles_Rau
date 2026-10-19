package com.rau.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rau.cafecampus.viewmodel.SesionViewModel

@Composable
fun SplashScreen(
    sesionViewModel: SesionViewModel,
    onNavegarLogin: () -> Unit,
    onNavegarCliente: () -> Unit,
    onNavegarAdmin: () -> Unit
) {
    val isLoggedIn by sesionViewModel.isLoggedIn.collectAsState(initial = false)
    val userRole by sesionViewModel.userRole.collectAsState(initial = "")

    LaunchedEffect(isLoggedIn, userRole) {
        if (isLoggedIn) {
            if (userRole == "ADMIN") {
                onNavegarAdmin()
            } else {
                onNavegarCliente()
            }
        } else {
            onNavegarLogin()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "☕ CafeCampus",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }
    }
}
