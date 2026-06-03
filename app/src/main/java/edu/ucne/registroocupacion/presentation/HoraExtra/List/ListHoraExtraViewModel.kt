package edu.ucne.registroocupacion.presentation.HoraExtra.List

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroocupacion.domain.Empleados.usecases.ObserveEmpleadoUseCase
import edu.ucne.registroocupacion.domain.HoraExtra.model.HoraExtra
import edu.ucne.registroocupacion.domain.HoraExtra.usecases.DeleteHoraExtraUseCase
import edu.ucne.registroocupacion.domain.HoraExtra.usecases.ObserveHoraExtraUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ListHoraExtraViewModel @Inject constructor(
    private val observeHorasExtrasUseCase: ObserveHoraExtraUseCase,
    private val observeEmpleadoUseCase: ObserveEmpleadoUseCase,
    private val deleteHoraExtraUseCase: DeleteHoraExtraUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ListHoraExtraUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            combine(
                observeHorasExtrasUseCase(),
                observeEmpleadoUseCase()
            ) { horasExtras, listaEmpleados ->
                _state.update {
                    it.copy(
                        registros = horasExtras,
                        empleados = listaEmpleados,
                        isLoading = false
                    )
                }
            }.collect {}
        }
    }

    fun deleteHoraExtra(horaExtra: HoraExtra) {
        viewModelScope.launch {
            deleteHoraExtraUseCase(horaExtra)
        }
    }
}