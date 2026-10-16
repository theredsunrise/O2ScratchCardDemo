package com.example.scratchcardo2demo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scratchcardo2demo.domain.`interface`.ScratchCardPersistenceRepository
import com.example.scratchcardo2demo.domain.usecase.ScratchCardUseCase
import com.example.scratchcardo2demo.presentation.mapper.toPresentationError
import com.example.scratchcardo2demo.presentation.mapper.toUi
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiIntent
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiLoadingAndError
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiState
import com.example.scratchcardo2demo.shared.assertIsNotMainThread
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ScratchCardScreenViewModel(
    dispatcher: CoroutineDispatcher,
    scratchCardUseCase: ScratchCardUseCase,
    persistenceRepository: ScratchCardPersistenceRepository,
) : ViewModel() {

    private val trigger = MutableSharedFlow<ScratchCardUiIntent>()

    private val repositoryFlow = persistenceRepository.state.map {
        //assertIsNotMainThread()
        it?.toUi(loadingAndError = ScratchCardUiLoadingAndError.Nothing) ?: ScratchCardUiState.UNSCRATCHED
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val scratchCardFlow: Flow<ScratchCardUiLoadingAndError> =
        trigger.filterIsInstance<ScratchCardUiIntent.Scratch>().flatMapLatest {
            flow {
                //assertIsNotMainThread()
                emit(ScratchCardUiLoadingAndError.Loading as ScratchCardUiLoadingAndError)
                //result is not emitted because it will be delivered later via persistenceRepository
                scratchCardUseCase()
                emit(ScratchCardUiLoadingAndError.Nothing)
            }.catch {
                if (it !is CancellationException) {
                    emit(value = ScratchCardUiLoadingAndError.Error(it.toPresentationError()))
                }
            }
        }.onStart { emit(ScratchCardUiLoadingAndError.Nothing) }

    val uiState: StateFlow<ScratchCardUiState> =
        combine(repositoryFlow, scratchCardFlow) { uiState, loadingAndError ->
            //assertIsNotMainThread()
            uiState.copy(loadingAndError = loadingAndError)
        }.flowOn(dispatcher).stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = ScratchCardUiState.UNSCRATCHED
        )

    fun intent(intent: ScratchCardUiIntent) {
        viewModelScope.launch {
            trigger.emit(intent)
        }
    }
}
