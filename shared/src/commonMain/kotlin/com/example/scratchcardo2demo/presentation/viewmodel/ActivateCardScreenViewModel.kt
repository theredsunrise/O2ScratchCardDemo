package com.example.scratchcardo2demo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scratchcardo2demo.domain.`interface`.ScratchCardPersistenceRepository
import com.example.scratchcardo2demo.domain.`interface`.ScratchCardActivatorManager
import com.example.scratchcardo2demo.presentation.mapper.toUI
import com.example.scratchcardo2demo.presentation.model.ActivateCardUIIntent
import com.example.scratchcardo2demo.presentation.model.ScratchCardUIPhase
import com.example.scratchcardo2demo.presentation.model.ScratchCardUIState
import com.example.scratchcardo2demo.shared.assertIsNotMainThread
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ActivateCardScreenViewModel(
    dispatcher: CoroutineDispatcher,
    activatorManager: ScratchCardActivatorManager,
    persistenceRepository: ScratchCardPersistenceRepository,
) : ViewModel() {

    private val trigger = MutableSharedFlow<ActivateCardUIIntent>()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val activateCardFlow: Flow<ScratchCardUIState> =
        trigger.filterIsInstance<ActivateCardUIIntent.Activate>().flatMapLatest {

            flow {
                assertIsNotMainThread()
                activatorManager.activate()
                emit(activatorManager.state.receive().getOrThrow().toUI())
            }.onStart {
                emit(ScratchCardUIState.Loading)
            }.catch {
                if (it !is CancellationException) {
                    emit(ScratchCardUIState.Error(it))
                }
            }

        }

    val uiState: StateFlow<ScratchCardUIState> = merge(persistenceRepository.state.map {
        assertIsNotMainThread()
        it?.toUI() ?: ScratchCardUIState.Success(ScratchCardUIPhase.Unscratched)
    }, activateCardFlow)
        .flowOn(dispatcher).stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(0),
            ScratchCardUIState.Loading
        )

    fun intent(intent: ActivateCardUIIntent) {
        viewModelScope.launch(start = CoroutineStart.UNDISPATCHED) {
            trigger.emit(intent)
        }
    }
}
