package com.example.scratchcardo2demo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scratchcardo2demo.domain.`interface`.ScratchCardActivatorManager
import com.example.scratchcardo2demo.domain.`interface`.ScratchCardPersistenceRepository
import com.example.scratchcardo2demo.presentation.mapper.toUi
import com.example.scratchcardo2demo.presentation.mapper.toUiResult
import com.example.scratchcardo2demo.presentation.model.ActivateCardUiIntent
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiResult
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiState
import com.example.scratchcardo2demo.shared.assertIsNotMainThread
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
        assertIsNotMainThread()
        it?.toUi(result = ScratchCardUiResult.Nothing) ?: ScratchCardUiState.UNSCRATCHED
    }

    private val activateFlow =
        activatorManager.state.map { it?.toUiResult() ?: ScratchCardUiResult.Nothing }

    val uiState: StateFlow<ScratchCardUiState> =
        combine(repositoryFlow, activateFlow) { uiState, uiResult ->
            assertIsNotMainThread()
            uiState.copy(uiResult = uiResult)
        }.flowOn(dispatcher).stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = ScratchCardUiState.UNSCRATCHED
        )

    fun intent(intent: ActivateCardUiIntent) {
        viewModelScope.launch(dispatcher) {
            if (intent is ActivateCardUiIntent.Activate) {
                activatorManager.activate()
            }
        }
    }
}
