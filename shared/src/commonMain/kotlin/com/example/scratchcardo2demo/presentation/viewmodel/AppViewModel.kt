package com.example.scratchcardo2demo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scratchcardo2demo.domain.`interface`.ScratchCardPersistenceRepository
import com.example.scratchcardo2demo.presentation.mapper.toUi
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiResult
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiState
import com.example.scratchcardo2demo.shared.assertIsNotMainThread
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AppViewModel(
    dispatcher: CoroutineDispatcher,
    persistenceRepository: ScratchCardPersistenceRepository,
) : ViewModel() {

    val uiState: StateFlow<ScratchCardUiState> = persistenceRepository.state.map {
        assertIsNotMainThread()
        it?.toUi(result = ScratchCardUiResult.Nothing) ?: ScratchCardUiState.UNSCRATCHED
    }.flowOn(dispatcher).stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = ScratchCardUiState.UNSCRATCHED
    )
}