package edu.ucne.registroocupacion.presentation.ocupacion.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditOcupacionScreen(
    ocupacionId: Int?,
    viewModel: EditOcupacionViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(ocupacionId) {
        viewModel.onEvent(EditOcupacionUIEvent.Load(ocupacionId ?: 0))
    }


    if (state.saved || state.deleted) {
        LaunchedEffect(Unit) {
            onNavigateBack()
        }
    }

    EditOcupacionBody(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditOcupacionBody(
    state: EditOcupacionUIState,
    onEvent: (EditOcupacionUIEvent) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (state.isNew) "Nueva Ocupación" else "Editar Ocupación") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Campo Descripción
                    OutlinedTextField(
                        label = { Text("Descripción") },
                        value = state.descripcion,
                        onValueChange = { onEvent(EditOcupacionUIEvent.DescripcionChange(it)) },
                        isError = state.descripcionError != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    state.descripcionError?.let {
                        Text(text = it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Campo Sueldo
                    OutlinedTextField(
                        label = { Text("Sueldo") },
                        value = state.sueldo?.toString() ?: "",
                        onValueChange = { onEvent(EditOcupacionUIEvent.SueldoChanged(it)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = state.sueldoError != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    state.sueldoError?.let {
                        Text(text = it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botones de Acción
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = { onEvent(EditOcupacionUIEvent.Save) },
                            enabled = !state.isSaving
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Guardar")
                        }

                        if (!state.isNew) {
                            OutlinedButton(
                                modifier = Modifier.weight(1f),
                                onClick = { onEvent(EditOcupacionUIEvent.Delete) },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditOcupacionPreview() {
    MaterialTheme {
        EditOcupacionBody(
            state = EditOcupacionUIState(
                descripcion = "Ingeniero",
                sueldo = 50000.0,
                isNew = false
            ),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}