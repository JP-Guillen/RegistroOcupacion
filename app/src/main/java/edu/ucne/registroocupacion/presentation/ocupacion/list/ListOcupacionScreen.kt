package edu.ucne.registroocupacion.presentation.ocupacion.list

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registroocupacion.domain.Ocupaciones.model.Ocupacion

@Composable
fun OcupacionListScreen(
    onDrawer: () -> Unit,
    goToOcupacion: (Int) -> Unit,
    createOcupacion: () -> Unit,
    viewModel: ListOcupacionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.navigateToCreate) {
        if (state.navigateToCreate) {
            createOcupacion()
            viewModel.onNavigationHandled()
        }
    }

    LaunchedEffect(state.navigateToEditId) {
        state.navigateToEditId?.let { id ->
            goToOcupacion(id)
            viewModel.onNavigationHandled()
        }
    }

    OcupacionListBody(
        state = state,
        onDrawer = onDrawer,
        onEvent = { event ->
            when (event) {
                is ListOcupacionUiEvent.Edit -> viewModel.onEvent(event)
                is ListOcupacionUiEvent.CreateNew -> viewModel.onEvent(event)
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OcupacionListBody(
    state: ListOcupacionUiState,
    onDrawer: () -> Unit,
    onEvent: (ListOcupacionUiEvent) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        state.message?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Listado de Ocupaciones") },
                navigationIcon = {
                    IconButton(onClick = onDrawer) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(ListOcupacionUiEvent.CreateNew) }) {
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
                items(state.ocupaciones) { ocupacion ->
                    OcupacionCard(
                        ocupacion = ocupacion,
                        onClick = { onEvent(ListOcupacionUiEvent.Edit(ocupacion.ocupacionId)) },
                        onDelete = { onEvent(ListOcupacionUiEvent.Delete(ocupacion.ocupacionId)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun OcupacionCard(
    ocupacion: Ocupacion,
    onClick: () -> Unit,
    onDelete: () -> Unit,
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
                Text(ocupacion.descripcion, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Sueldo: $${ocupacion.sueldo}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OcupacionListPreview() {
    MaterialTheme {
        OcupacionListBody(
            state = ListOcupacionUiState(
                ocupaciones = listOf(
                    Ocupacion(1, "Ingeniero en Sistemas", 85000.0),
                    Ocupacion(2, "Analista de Datos", 75000.0)
                )
            ),
            onDrawer = {},
            onEvent = {}
        )
    }
}