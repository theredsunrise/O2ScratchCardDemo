package com.example.scratchcardo2demo.presentation.model

sealed interface ActivateCardUiIntent {
    data object Activate : ActivateCardUiIntent
}
