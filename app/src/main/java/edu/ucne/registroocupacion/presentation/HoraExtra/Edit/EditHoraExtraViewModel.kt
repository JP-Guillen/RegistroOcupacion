package edu.ucne.registroocupacion.presentation.HoraExtra.Edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroocupacion.domain.Empleados.usecases.GetEmpleadoUseCase
import edu.ucne.registroocupacion.domain.Empleados.usecases.ObserveEmpleadoUseCase
import edu.ucne.registroocupacion.domain.HoraExtra.usecases.CalcularHorasExtrasUseCase
import edu.ucne.registroocupacion.domain.HoraExtra.usecases.DeleteHoraExtraUseCase
import edu.ucne.registroocupacion.domain.HoraExtra.usecases.GetHoraExtraUseCase
import edu.ucne.registroocupacion.domain.HoraExtra.usecases.UpsertHoraExtraUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EditHoraExtraViewModel@Inject constructor(
    private val getHoraExtraUseCase: GetHoraExtraUseCase,
    private val upsertHoraExtraUseCase: UpsertHoraExtraUseCase,
    private val deleteHoraExtraUseCase: DeleteHoraExtraUseCase,
    private val observeEmpleadoUseCase: ObserveEmpleadoUseCase,
    private val getEmpleadoUseCase: GetEmpleadoUseCase,
    private val calcularHorasExtrasUseCase: CalcularHorasExtrasUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EditHoraExtraUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observeEmpleadoUseCase().collect { lista ->
                _state.update { it.copy(empleados = lista) }
            }
        }
    }

    fun onEvent(event: EditHoraExtraUiEvent) {
        when (event) {
            is EditHoraExtraUiEvent.Load -> onLoad(event.id)
            is EditHoraExtraUiEvent.EmpleadoChanged -> {
                _state.update { it.copy(empleadoId = event.empleado.empleadoId ?: 0, empleadoSeleccionado = event.empleado, empleadoError = null) }
                recalcular()
            }
            is EditHoraExtraUiEvent.HorasSemanalesChanged -> {
                _state.update { it.copy(horasSemanales = event.value, horasSemanalesError = null) }
                recalcular()
            }
            is EditHoraExtraUiEvent.HorasNocturnasChanged -> {
                _state.update { it.copy(horasNocturnas = event.value) }
                recalcular()
            }
            EditHoraExtraUiEvent.Save -> onSave()
            EditHoraExtraUiEvent.Delete -> onDelete()
        }
    }

    private fun onLoad(id: Int?) {
        if (id == null || id == 0) return
        viewModelScope.launch {
            getHoraExtraUseCase(id)?.let { registro ->
                val empleado = getEmpleadoUseCase(registro.empleadoId)
                _state.update {
                    it.copy(
                        isNew = false,
                        horaExtraId = registro.horaExtraId,
                        empleadoId = registro.empleadoId,
                        empleadoSeleccionado = empleado,
                        horasSemanales = registro.horasTrabajadasSemanales.toString(),
                        horasNocturnas = registro.horasNocturnas.toString(),
                        calculoActual = registro
                    )
                }
            }
        }
    }

    private fun recalcular() {
        val empleado = _state.value.empleadoSeleccionado ?: return
        val hSemanales = _state.value.horasSemanales.toDoubleOrNull() ?: 0.0
        val hNocturnas = _state.value.horasNocturnas.toDoubleOrNull() ?: 0.0

        if (hSemanales > 0.0) {
            val resultado = calcularHorasExtrasUseCase(
                empleado, hSemanales, hNocturnas,
                _state.value.horaExtraId, _state.value.calculoActual?.fechaRegistro
            )
            _state.update { it.copy(calculoActual = resultado) }
        }
    }

    private fun onSave() {
        val calculo = _state.value.calculoActual
        if (calculo == null) {
            _state.update { it.copy(horasSemanalesError = "Debe ingresar horas válidas") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            upsertHoraExtraUseCase(calculo).onSuccess {
                _state.update { it.copy(isSaving = false, saved = true) }
            }.onFailure { e ->
                _state.update { it.copy(isSaving = false, horasSemanalesError = e.message) }
            }
        }
    }

    private fun onDelete() {
        val calculo = _state.value.calculoActual ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            deleteHoraExtraUseCase(calculo)
            _state.update { it.copy(isDeleting = false, deleted = true) }
        }
    }
}