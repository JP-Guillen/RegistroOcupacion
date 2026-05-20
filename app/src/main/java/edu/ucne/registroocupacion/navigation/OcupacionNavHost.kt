package edu.ucne.registroocupacion.presentation.Navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registroocupacion.presentation.empleado.edit.EditEmpleadoScreen
import edu.ucne.registroocupacion.presentation.empleado.list.EmpleadoListScreen
import edu.ucne.registroocupacion.presentation.ocupacion.edit.EditOcupacionScreen
import edu.ucne.registroocupacion.presentation.ocupacion.list.OcupacionListScreen
import edu.ucne.registroocupacion.navigation.Screen
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
                EmpleadoListScreen(
                    onDrawer = { scope.launch { drawerState.open() } },
                    goToEmpleado = { id -> navHostController.navigate(Screen.Empleado(id)) },
                    createEmpleado = { navHostController.navigate(Screen.Empleado(0)) }
                )
            }

            composable<Screen.Empleado> {
                val args = it.toRoute<Screen.Empleado>()
                EditEmpleadoScreen(
                    empleadoId = args.EmpleadoId,
                    onNavigateBack = { navHostController.navigateUp() }
                )
            }

            composable<Screen.OcupacionList> {
                OcupacionListScreen(
                    onDrawer = { scope.launch { drawerState.open() } },
                    goToOcupacion = { id -> navHostController.navigate(Screen.Ocupacion(id)) },
                    createOcupacion = { navHostController.navigate(Screen.Ocupacion(0)) }
                )
            }

            composable<Screen.Ocupacion> {
                val args = it.toRoute<Screen.Ocupacion>()
                EditOcupacionScreen(
                    ocupacionId = args.ocupacionId,
                    onNavigateBack = { navHostController.navigateUp() }
                )
            }
        }
    }
}