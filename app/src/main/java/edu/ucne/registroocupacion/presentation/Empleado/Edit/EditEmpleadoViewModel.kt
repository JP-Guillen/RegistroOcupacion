package edu.ucne.registroocupacion.presentation.empleado.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroocupacion.domain.Empleados.model.Empleado
import edu.ucne.registroocupacion.domain.Empleados.usecases.DeleteEmpleadoUseCase
import edu.ucne.registroocupacion.domain.Empleados.usecases.GetEmpleadoUseCase
import edu.ucne.registroocupacion.domain.Empleados.usecases.UpsertEmpleadoUseCase
import edu.ucne.registroocupacion.domain.Empleados.usecases.ValidateEmpleadoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EditEmpleadoViewModel @Inject constructor(
    private val getEmpleadoUseCase: GetEmpleadoUseCase,
    private val upsertEmpleadoUseCase: UpsertEmpleadoUseCase,
    private val deleteEmpleadoUseCase: DeleteEmpleadoUseCase,
    private val validateEmpleadoUseCase: ValidateEmpleadoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EditEmpleadoUIState())
    val state: StateFlow<EditEmpleadoUIState> = _state.asStateFlow()

    private fun validateNombre(nombre: String) {
        viewModelScope.launch {
            val result = validateEmpleadoUseCase(
                nombre = nombre,
                fecha = _state.value.fechaIngreso,
                sueldo = _state.value.sueldo ?: 0.0,
                currentEmpleadoId = _state.value.empleadoId
            )
            _state.update { it.copy(nombreError = result.nombreError) }
        }
    }

    private fun validateSueldo(sueldo: Double) {
        viewModelScope.launch {
            val result = validateEmpleadoUseCase(
                nombre = _state.value.nombre,
                fecha = _state.value.fechaIngreso,
                sueldo = sueldo,
                currentEmpleadoId = _state.value.empleadoId
            )
            _state.update { it.copy(sueldoError = result.sueldoError) }
        }
    }

    private fun validateFecha(fecha: LocalDate?) {
        viewModelScope.launch {
            val result = validateEmpleadoUseCase(
                nombre = _state.value.nombre,
                fecha = fecha,
                sueldo = _state.value.sueldo ?: 0.0,
                currentEmpleadoId = _state.value.empleadoId
            )
            _state.update { it.copy(fechaIngresoError = result.fechaError) }
        }
    }

    fun onEvent(event: EditEmpleadoUIEvent) {
        when (event) {
            is EditEmpleadoUIEvent.Load -> onLoad(event.id)
            is EditEmpleadoUIEvent.NombreChanged -> {
                _state.update { it.copy(nombre = event.value, nombreError = null) }
                validateNombre(event.value)
            }
            is EditEmpleadoUIEvent.SueldoChanged -> {
                val sueldoDouble = event.value
                _state.update { it.copy(sueldo = sueldoDouble, sueldoError = null) }
                validateSueldo(sueldoDouble)
            }
            is EditEmpleadoUIEvent.SexoChanged -> {
                _state.update { it.copy(sexo = event.value) }
            }
            is EditEmpleadoUIEvent.FechaIngresoChanged -> {
                _state.update { it.copy(fechaIngreso = event.value, fechaIngresoError = null) }
                validateFecha(event.value)
            }
            EditEmpleadoUIEvent.Save -> onSave()
            EditEmpleadoUIEvent.Delete -> onDelete()
        }
    }

    private fun onLoad(id: Int?) {
        if (id == null || id == 0) {
            _state.update { it.copy(isNew = true, empleadoId = null) }
            return
        }
        viewModelScope.launch {
            val empleado = getEmpleadoUseCase(id)
            empleado?.let { item ->
                _state.update {
                    it.copy(
                        isNew = false,
                        empleadoId = item.empleadoId,
                        nombre = item.nombres,
                        sueldo = item.sueldo,
                        sexo = item.sexo,
                        fechaIngreso = item.fechaIngreso
                    )
                }
            }
        }
    }

    private fun onSave() {
        viewModelScope.launch {
            val validation = validateEmpleadoUseCase(
                nombre = _state.value.nombre,
                fecha = _state.value.fechaIngreso,
                sueldo = _state.value.sueldo ?: 0.0,
                currentEmpleadoId = _state.value.empleadoId
            )

            if (!validation.isValid) {
                _state.update {
                    it.copy(
                        nombreError = validation.nombreError,
                        sueldoError = validation.sueldoError,
                        fechaIngresoError = validation.fechaError
                    )
                }
                return@launch
            }

            _state.update { it.copy(isSaving = true) }
            try {
                val empleado = Empleado(
                    empleadoId = _state.value.empleadoId ?: 0,
                    nombres = _state.value.nombre,
                    sueldo = _state.value.sueldo ?: 0.0,
                    sexo = _state.value.sexo,
                    fechaIngreso = _state.value.fechaIngreso ?: LocalDate.now()
                )

                val result = upsertEmpleadoUseCase(empleado)

                result.onSuccess { generatedId ->
                    _state.update { it.copy(isSaving = false, saved = true, empleadoId = generatedId) }
                }.onFailure { exception ->
                    _state.update { it.copy(isSaving = false, nombreError = exception.message) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isSaving = false, nombreError = e.message) }
            }
        }
    }

    private fun onDelete() {
        val id = _state.value.empleadoId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            try {
                deleteEmpleadoUseCase(id)
                _state.update { it.copy(isDeleting = false, deleted = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isDeleting = false, nombreError = e.message) }
            }
        }
    }
}