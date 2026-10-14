package com.example.scratchcardo2demo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scratchcardo2demo.domain.`interface`.ScratchCardPersistenceRepository
import com.example.scratchcardo2demo.domain.usecase.ScratchCardUseCase
import com.example.scratchcardo2demo.presentation.mapper.toUI
import com.example.scratchcardo2demo.presentation.model.ScratchCardUIIntent
import com.example.scratchcardo2demo.presentation.model.ScratchCardUIPhase
import com.example.scratchcardo2demo.presentation.model.ScratchCardUIState
import com.example.scratchcardo2demo.shared.assertIsNotMainThread
import com.example.scratchcardo2demo.shared.isMainThread
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

class ScratchCardScreenViewModel(
    dispatcher: CoroutineDispatcher,
    scratchCardUseCase: ScratchCardUseCase,
    persistenceRepository: ScratchCardPersistenceRepository,
) : ViewModel() {

    private val trigger = MutableSharedFlow<ScratchCardUIIntent>()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val scratchCardFlow: Flow<ScratchCardUIState> =
        trigger.filterIsInstance<ScratchCardUIIntent.Scratch>().flatMapLatest {

            flow {
                assertIsNotMainThread()
                emit(scratchCardUseCase.invoke().toUI())
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
    }, scratchCardFlow)
        .flowOn(dispatcher).stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(0),
            ScratchCardUIState.Loading
        )

    fun intent(intent: ScratchCardUIIntent) {
        viewModelScope.launch(start = CoroutineStart.UNDISPATCHED) {
            trigger.emit(intent)
        }
    }
}