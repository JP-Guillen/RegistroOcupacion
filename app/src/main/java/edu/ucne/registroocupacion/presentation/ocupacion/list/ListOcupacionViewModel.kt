package edu.ucne.registroocupacion.presentation.ocupacion.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroocupacion.domain.Ocupaciones.usecases.DeleteOcupacionUseCase
import edu.ucne.registroocupacion.domain.Ocupaciones.usecases.ObserveOcupacionUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ListOcupacionViewModel @Inject constructor(
    private val observeOcupacionUseCase: ObserveOcupacionUseCase,
    private val deleteOcupacionUseCase: DeleteOcupacionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ListOcupacionUiState(isLoading = true))
    val state: StateFlow<ListOcupacionUiState> = _state.asStateFlow()

    init {
        onEvent(ListOcupacionUiEvent.Load)
    }

    fun onEvent(event: ListOcupacionUiEvent) {
        when (event) {
            ListOcupacionUiEvent.Load -> observeOcupaciones()
            is ListOcupacionUiEvent.Delete -> onDelete(event.id)
            ListOcupacionUiEvent.CreateNew -> _state.update { it.copy(navigateToCreate = true) }
            is ListOcupacionUiEvent.Edit -> _state.update { it.copy(navigateToEditId = event.id) }
            is ListOcupacionUiEvent.ShowMessage -> _state.update { it.copy(message = event.message) }
            else -> {}
        }
    }

    private fun observeOcupaciones() {
        viewModelScope.launch {
            observeOcupacionUseCase().collectLatest { ocupacionesList ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        ocupaciones = ocupacionesList,
                        message = null
                    )
                }
            }
        }
    }

    private fun onDelete(id: Int) {
        viewModelScope.launch {
            try {
                deleteOcupacionUseCase(id)
                onEvent(ListOcupacionUiEvent.ShowMessage("Ocupación eliminada"))
            } catch (e: Exception) {
                onEvent(ListOcupacionUiEvent.ShowMessage("Error al eliminar: ${e.message}"))
            }
        }
    }

    fun onNavigationHandled() {
        _state.update {
            it.copy(
                navigateToCreate = false,
                navigateToEditId = null,
                message = null
            )
        }
    }
}