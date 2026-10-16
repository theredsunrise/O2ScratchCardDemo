package com.example.scratchcardo2demo.presentation.model

import com.example.scratchcardo2demo.presentation.mapper.PresentationError

sealed interface ScratchCardUiLoadingAndError {
    data object Nothing : ScratchCardUiLoadingAndError
    data object Loading : ScratchCardUiLoadingAndError
    data class Error(val error: PresentationError) : ScratchCardUiLoadingAndError

    val isLoading: Boolean get() = this == Loading
    val isError: Boolean get() = this is Error
}
