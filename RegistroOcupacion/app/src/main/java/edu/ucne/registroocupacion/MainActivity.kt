package edu.ucne.registroocupacion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.registroocupacion.presentation.ocupacion.navigation.OcupacionNavHost
import edu.ucne.registroocupacion.ui.theme.RegistroOcupacionTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistroOcupacionTheme {
                val navHostController = rememberNavController()
                OcupacionNavHost(navController = navHostController)
            }
        }
    }
}

