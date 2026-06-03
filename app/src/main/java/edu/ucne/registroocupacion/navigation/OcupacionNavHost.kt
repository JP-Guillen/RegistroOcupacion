package edu.ucne.registroocupacion.presentation.Navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.ucne.registroocupacion.navigation.Screen
import edu.ucne.registroocupacion.presentation.Empleado.EmpleadoAdaptativeScreen
import edu.ucne.registroocupacion.presentation.horaextra.HoraExtraAdaptativeScreen
import edu.ucne.registroocupacion.presentation.ocupacion.OcupacionAdaptativeScreen
import kotlinx.coroutines.launch

@Composable
fun OcupacionNavHost(
    navHostController: NavHostController
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    DrawerMenu(
        drawerState = drawerState,
        navHostController = navHostController
    ) {
        NavHost(
            navController = navHostController,
            startDestination = Screen.EmpleadoList
        ) {
            composable<Screen.EmpleadoList> {
                EmpleadoAdaptativeScreen(
                    onDrawer = { scope.launch { drawerState.open() } }
                )
            }

            composable<Screen.OcupacionList> {
                OcupacionAdaptativeScreen(
                    onDrawer = { scope.launch { drawerState.open() } }
                )
            }

            composable<Screen.HoraExtraList> {
                HoraExtraAdaptativeScreen(
                    onDrawer = { scope.launch { drawerState.open() } }
                )
            }
        }
    }
}