package edu.ucne.registroocupacion.presentation.Navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.window.core.layout.WindowWidthSizeClass
import edu.ucne.registroocupacion.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun DrawerMenu(
    drawerState: DrawerState,
    navHostController: NavHostController,
    content: @Composable () -> Unit
) {
    val selectedItem = remember { mutableStateOf("Empleados") }
    val scope = rememberCoroutineScope()
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isExpanded = windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED

    val items = listOf(
        Triple("Empleados", Icons.Filled.People, Screen.EmpleadoList),
        Triple("Ocupaciones", Icons.Filled.Build, Screen.OcupacionList),
        Triple("Horas Extras", Icons.Filled.AccessTime, Screen.HoraExtraList)
    )

    fun handleItemClick(destination: Screen, item: String) {
        navHostController.navigate(destination) {
            launchSingleTop = true
        }
        selectedItem.value = item
        scope.launch { drawerState.close() }
    }

    if (isExpanded) {
        Row {
            NavigationRail {
                Spacer(Modifier.height(16.dp))
                items.forEach { (title, icon, destination) ->
                    NavigationRailItem(
                        icon = { Icon(icon, contentDescription = title) },
                        label = { Text(title) },
                        selected = selectedItem.value == title,
                        onClick = { handleItemClick(destination, title) }
                    )
                }
            }
            content()
        }
    } else {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.width(280.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Control de Empleados",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(16.dp)
                    )
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn {
                        item {
                            items.forEach { (title, icon, destination) ->
                                DrawerItem(
                                    title = title,
                                    icon = icon,
                                    isSelected = selectedItem.value == title
                                ) {
                                    handleItemClick(destination, title)
                                }
                            }
                        }
                    }
                }
            }
        ) {
            content()
        }
    }
}