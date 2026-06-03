package edu.ucne.registroocupacion.presentation.horaextra

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import edu.ucne.registroocupacion.presentation.HoraExtra.Edit.EditHoraExtraScreen
import edu.ucne.registroocupacion.presentation.HoraExtra.List.ListHoraExtraScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HoraExtraAdaptativeScreen(
    onDrawer: () -> Unit
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    val scope = rememberCoroutineScope()
    var selectedHoraExtraId by remember { mutableStateOf<Int?>(null) }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            ListHoraExtraScreen(
                onDrawer = onDrawer,
                goToEditHoraExtra = { id ->
                    selectedHoraExtraId = id
                    scope.launch {
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                    }
                },
                createHoraExtra = {
                    selectedHoraExtraId = 0
                    scope.launch {
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                    }
                }
            )
        },
        detailPane = {
            selectedHoraExtraId?.let { id ->
                EditHoraExtraScreen(
                    horaExtraId = id,
                    onBack = {
                        scope.launch {
                            if (navigator.canNavigateBack()) {
                                navigator.navigateBack()
                            } else {
                                selectedHoraExtraId = null
                            }
                        }
                    }
                )
            }
        }
    )
}