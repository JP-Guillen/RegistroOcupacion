package edu.ucne.registroocupacion.presentation.empleado.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registroocupacion.presentation.empleado.edit.EditEmpleadoUIEvent
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEmpleadoScreen(
    empleadoId: Int?,
    onNavigateBack: () -> Unit,
    viewModel: EditEmpleadoViewModel = hiltViewModel()
) {
    LaunchedEffect(empleadoId) {
        viewModel.onEvent(EditEmpleadoUIEvent.Load(empleadoId))
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.saved, state.deleted) {
        if (state.saved || state.deleted) {
            onNavigateBack()
        }
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var expandedSexo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (state.isNew) "Nuevo Empleado" else "Editar Empleado") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Atrás")
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
            OutlinedTextField(
                value = state.fechaIngreso?.toString() ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha Ingreso") },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                    }
                },
                isError = state.fechaIngresoError != null,
                supportingText = { state.fechaIngresoError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val fecha = Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                viewModel.onEvent(EditEmpleadoUIEvent.FechaIngresoChanged(fecha))
                            }
                            showDatePicker = false
                        }) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = state.nombre,
                onValueChange = { viewModel.onEvent(EditEmpleadoUIEvent.NombreChanged(it)) },
                label = { Text("Nombres") },
                isError = state.nombreError != null,
                supportingText = { state.nombreError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = expandedSexo,
                onExpandedChange = { expandedSexo = !expandedSexo }
            ) {
                OutlinedTextField(
                    value = state.sexo,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Sexo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedSexo) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    isError = state.sexoError != null,
                    supportingText = { state.sexoError?.let { Text(it) } }
                )
                ExposedDropdownMenu(
                    expanded = expandedSexo,
                    onDismissRequest = { expandedSexo = false }
                ) {
                    listOf("Masculino", "Femenino").forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                viewModel.onEvent(EditEmpleadoUIEvent.SexoChanged(opcion))
                                expandedSexo = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = state.sueldo?.toString() ?: "",
                onValueChange = { texto ->
                    val valorDouble = texto.toDoubleOrNull() ?: 0.0
                    viewModel.onEvent(EditEmpleadoUIEvent.SueldoChanged(valorDouble))
                },
                label = { Text("Sueldo") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = state.sueldoError != null,
                supportingText = { state.sueldoError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { viewModel.onEvent(EditEmpleadoUIEvent.Save) },
                    enabled = !state.isSaving,
                    modifier = Modifier.weight(1f)
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Guardar")
                    }
                }

                if (!state.isNew) {
                    Spacer(Modifier.width(8.dp))
                    OutlinedButton(
                        onClick = { viewModel.onEvent(EditEmpleadoUIEvent.Delete) },
                        enabled = !state.isDeleting,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}