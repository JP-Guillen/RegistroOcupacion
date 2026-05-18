package edu.ucne.registroocupacion.presentation.ocupacion.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroocupacion.domain.Ocupaciones.model.Ocupacion
import edu.ucne.registroocupacion.domain.Ocupaciones.usecases.DeleteOcupacionUseCase
import edu.ucne.registroocupacion.domain.Ocupaciones.usecases.GetOcupacionUseCase
import edu.ucne.registroocupacion.domain.Ocupaciones.usecases.UpsertocupacionUseCase
import edu.ucne.registroocupacion.domain.Ocupaciones.usecases.ValidateOcupacionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditOcupacionViewModel @Inject constructor(
    private val getOcupacionUseCase: GetOcupacionUseCase,
    private val upsertocupacionUseCase: UpsertocupacionUseCase,
    private val deleteOcupacionUseCase: DeleteOcupacionUseCase,
    private val validateOcupacionUseCase: ValidateOcupacionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EditOcupacionUIState())
    val state: StateFlow<EditOcupacionUIState> = _state.asStateFlow()

    private fun validateDescripcion(descripcion: String) {
        viewModelScope.launch {
            val result = validateOcupacionUseCase(
                descripcion = descripcion,
                sueldo = _state.value.sueldo,
                currentOcupacionId = _state.value.ocupacionId
            )
            _state.update { it.copy(descripcionError = result.descripcionError) }
        }
    }

    private fun validateSueldo(sueldo: Double?) {
        viewModelScope.launch {
            val result = validateOcupacionUseCase(
                descripcion = _state.value.descripcion,
                sueldo = sueldo,
                currentOcupacionId = _state.value.ocupacionId
            )
            _state.update { it.copy(sueldoError = result.sueldoError) }
        }
    }

    fun onEvent(event: EditOcupacionUIEvent) {
        when (event) {
            is EditOcupacionUIEvent.Load -> onLoad(event.id)
            is EditOcupacionUIEvent.DescripcionChange -> {
                _state.update { it.copy(descripcion = event.value, descripcionError = null) }
                validateDescripcion(event.value)
            }
            is EditOcupacionUIEvent.SueldoChanged -> {
                val sueldoDouble = event.value.toDoubleOrNull()
                _state.update { it.copy(sueldo = sueldoDouble, sueldoError = null) }
                validateSueldo(sueldoDouble)
            }
            EditOcupacionUIEvent.Save -> onSave()
            EditOcupacionUIEvent.Delete -> onDelete()
        }
    }

    private fun onLoad(id: Int?) {
        if (id == null || id == 0) {
            _state.update { it.copy(isNew = true, ocupacionId = null) }
            return
        }
        viewModelScope.launch {
            val ocupacion = getOcupacionUseCase(id)
            ocupacion?.let { item ->
                _state.update {
                    it.copy(
                        isNew = false,
                        ocupacionId = item.ocupacionId,
                        descripcion = item.descripcion,
                        sueldo = item.sueldo
                    )
                }
            }
        }
    }

    private fun onSave() {
        viewModelScope.launch {
            val validation = validateOcupacionUseCase(
                descripcion = _state.value.descripcion,
                sueldo = _state.value.sueldo,
                currentOcupacionId = _state.value.ocupacionId
            )

            if (!validation.isValid) {
                _state.update {
                    it.copy(
                        descripcionError = validation.descripcionError,
                        sueldoError = validation.sueldoError
                    )
                }
                return@launch
            }

            _state.update { it.copy(isSaving = true) }
            try {
                val ocupacion = Ocupacion(
                    ocupacionId = _state.value.ocupacionId ?: 0,
                    descripcion = _state.value.descripcion,
                    sueldo = _state.value.sueldo ?: 0.0
                )
                upsertocupacionUseCase(ocupacion)
                _state.update { it.copy(isSaving = false, saved = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isSaving = false, descripcionError = e.message) }
            }
        }
    }

    private fun onDelete() {
        val id = _state.value.ocupacionId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            try {
                deleteOcupacionUseCase(id)
                _state.update { it.copy(isDeleting = false, deleted = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isDeleting = false, descripcionError = e.message) }
            }
        }
    }
}