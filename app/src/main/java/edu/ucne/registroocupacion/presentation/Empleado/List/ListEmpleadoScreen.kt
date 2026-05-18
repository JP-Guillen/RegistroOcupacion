package edu.ucne.registroocupacion.presentation.empleado.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registroocupacion.domain.Empleados.model.Empleado
import java.time.LocalDate

@Composable
fun EmpleadoListScreen(
    onDrawer: () -> Unit,
    goToEmpleado: (Int) -> Unit,
    createEmpleado: () -> Unit,
    viewModel: ListEmpleadoViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    EmpleadoListBody(
        state = state,
        onDrawer = onDrawer,
        onEvent = { event ->
            when (event) {
                is ListEmpleadoUiEvent.Edit -> goToEmpleado(event.id)
                is ListEmpleadoUiEvent.CreateNew -> createEmpleado()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmpleadoListBody(
    state: ListEmpleadoUiState,
    onDrawer: () -> Unit,
    onEvent: (ListEmpleadoUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Listado de Empleados") },
                navigationIcon = {
                    IconButton(onClick = onDrawer) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(ListEmpleadoUiEvent.CreateNew) }) {
                Text("+")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(state.empleados) { empleado ->
                    EmpleadoCard(
                        empleado = empleado,
                        onClick = { onEvent(ListEmpleadoUiEvent.Edit(empleado.empleadoId)) },
                        onDelete = { onEvent(ListEmpleadoUiEvent.Delete(empleado.empleadoId)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmpleadoCard(
    empleado: Empleado,
    onClick: () -> Unit,
    onDelete: (Int) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(empleado.nombres, style = MaterialTheme.typography.titleMedium)
                Text("Sueldo: $${empleado.sueldo}", style = MaterialTheme.typography.bodyMedium)
                Text("Sexo: ${empleado.sexo} | Ingreso: ${empleado.fechaIngreso}", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = { onDelete(empleado.empleadoId) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmpleadoListBodyPreview() {
    MaterialTheme {
        val state = ListEmpleadoUiState(
            isLoading = false,
            empleados = listOf(
                Empleado(empleadoId = 1, nombres = "Juan Pérez", sueldo = 35000.0, sexo = "M", fechaIngreso = LocalDate.now()),
                Empleado(empleadoId = 2, nombres = "María Rodriguez", sueldo = 45000.0, sexo = "F", fechaIngreso = LocalDate.now())
            )
        )
        EmpleadoListBody(state, onDrawer = {}, onEvent = {})
    }
}