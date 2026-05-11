package edu.ucne.registroocupacion.presentation.ocupacion.edit

interface EditOcupacionUIEvent {
    data class Load(val id: Int?) : EditOcupacionUIEvent
    data class DescripcionChange (val  value: String): EditOcupacionUIEvent
    data class SueldoChanged(val value: String) : EditOcupacionUIEvent
    data object Save : EditOcupacionUIEvent
    data object Delete : EditOcupacionUIEvent
}