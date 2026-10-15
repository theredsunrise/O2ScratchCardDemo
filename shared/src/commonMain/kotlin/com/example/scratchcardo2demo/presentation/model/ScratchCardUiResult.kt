package com.example.scratchcardo2demo.presentation.model

import com.example.scratchcardo2demo.presentation.mapper.PresentationError

sealed interface ScratchCardUiResult {
    data object Nothing : ScratchCardUiResult
    data object Loading : ScratchCardUiResult
    data class Error(val error: PresentationError) : ScratchCardUiResult

    val isLoading: Boolean get() = this == Loading
    val isSuccess: Boolean get() = this !is Error
    val isError: Boolean get() = this is Error
}
