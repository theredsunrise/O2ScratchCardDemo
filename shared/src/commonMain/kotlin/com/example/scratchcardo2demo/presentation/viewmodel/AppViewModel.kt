package com.example.scratchcardo2demo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scratchcardo2demo.domain.`interface`.ScratchCardPersistenceRepository
import com.example.scratchcardo2demo.presentation.mapper.toUI
import com.example.scratchcardo2demo.presentation.model.ScratchCardUIPhase
import com.example.scratchcardo2demo.presentation.model.ScratchCardUIState
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

    val uiState: StateFlow<ScratchCardUIState> = persistenceRepository.state.map {
        assertIsNotMainThread()
        it?.toUI() ?: ScratchCardUIState.Success(ScratchCardUIPhase.Unscratched)
    }.flowOn(dispatcher).stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        ScratchCardUIState.Loading
    )
}