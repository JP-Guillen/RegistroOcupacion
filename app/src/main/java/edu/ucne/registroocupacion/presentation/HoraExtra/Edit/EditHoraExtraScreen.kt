package edu.ucne.registroocupacion.presentation.HoraExtra.Edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHoraExtraScreen(
    horaExtraId: Int?,
    onBack: () -> Unit,
    viewModel: EditHoraExtraViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(horaExtraId) {
        viewModel.onEvent(EditHoraExtraUiEvent.Load(horaExtraId))
    }

    LaunchedEffect(state.saved, state.deleted) {
        if (state.saved || state.deleted) onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isNew) "Agregar Horas Extras" else "Modificar Registro") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Atrás") }
                },
                actions = {
                    if (!state.isNew) {
                        IconButton(onClick = { viewModel.onEvent(EditHoraExtraUiEvent.Delete) }) {
                            Icon(Icons.Default.Delete, "Eliminar")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
            ) {
                OutlinedTextField(
                    value = state.empleadoSeleccionado?.nombres ?: "Seleccione un Empleado",
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = { Text("Empleado") },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, "Dropdown") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    state.empleados.forEach { emp ->
                        DropdownMenuItem(
                            text = { Text(emp.nombres) },
                            onClick = {
                                viewModel.onEvent(EditHoraExtraUiEvent.EmpleadoChanged(emp))
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = state.horasSemanales,
                onValueChange = { viewModel.onEvent(EditHoraExtraUiEvent.HorasSemanalesChanged(it)) },
                label = { Text("Horas Semanales Trabajadas") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.horasNocturnas,
                onValueChange = { viewModel.onEvent(EditHoraExtraUiEvent.HorasNocturnasChanged(it)) },
                label = { Text("Horas Nocturnas Trabajadas") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            state.calculoActual?.let { calc ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Resultados del cálculo:", fontWeight = FontWeight.Bold)
                        HorizontalDivider()
                        Text("Horas Normales: ${calc.horasNormales}")
                        Text("Horas al 35%: ${calc.horasA35}")
                        Text("Horas al 100%: ${calc.horasA100}")
                        Text("Monto Total: RD$ ${calc.montoTotalHorasExtras}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                }
            }

            state.horasSemanalesError?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.onEvent(EditHoraExtraUiEvent.Save) },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.calculoActual != null && !state.isSaving
            ) {
                Text("Guardar Cambios")
            }
        }
    }
}