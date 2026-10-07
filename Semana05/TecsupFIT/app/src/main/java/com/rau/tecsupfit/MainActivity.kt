package com.rau.tecsupfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rau.tecsupfit.navigation.FitMainApp
import com.rau.tecsupfit.ui.theme.TecsupFITTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TecsupFITTheme {
                FitMainApp()
            }
        }
    }
}
