package com.example.scratchcardo2demo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scratchcardo2demo.domain.`interface`.ScratchCardActivatorManager
import com.example.scratchcardo2demo.domain.`interface`.ScratchCardPersistenceRepository
import com.example.scratchcardo2demo.presentation.mapper.toLoadingAndError
import com.example.scratchcardo2demo.presentation.mapper.toUi
import com.example.scratchcardo2demo.presentation.model.ActivateCardUiIntent
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiLoadingAndError
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ActivateCardScreenViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val activatorManager: ScratchCardActivatorManager,
    persistenceRepository: ScratchCardPersistenceRepository,
) : ViewModel() {

    private val repositoryFlow = persistenceRepository.state.map {
        //assertIsNotMainThread()
        it?.toUi(loadingAndError = ScratchCardUiLoadingAndError.Nothing)
            ?: ScratchCardUiState.UNSCRATCHED
    }

    private val activateFlow =
        activatorManager.state.map {
            it?.toLoadingAndError() ?: ScratchCardUiLoadingAndError.Nothing
        }

    val uiState: StateFlow<ScratchCardUiState> =
        combine(repositoryFlow, activateFlow) { uiState, uiLoadingAndError ->
            //assertIsNotMainThread()
            //resolving only loading and error states, data will be delivered later in the repository flow
            uiState.copy(loadingAndError = uiLoadingAndError)
        }.flowOn(dispatcher).stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = ScratchCardUiState.UNSCRATCHED
        )

    fun intent(intent: ActivateCardUiIntent) {
        viewModelScope.launch(dispatcher) {
            if (intent is ActivateCardUiIntent.Activate) {
                //result of this operation will be handled later in the repository flow.
                activatorManager.activate()
            }
        }
    }
}
