package com.example.scratchcardo2demo.presentation.model

import kotlin.uuid.Uuid

sealed interface ScratchCardState {
    data object UnScratched : ScratchCardState
    data class Scratched(val id: Uuid) : ScratchCardState
}

sealed interface ScratchCardUIState {
    data object Loading : ScratchCardUIState
    data class Success(val state: ScratchCardState) : ScratchCardUIState
    data class Error(val error: Throwable)
}