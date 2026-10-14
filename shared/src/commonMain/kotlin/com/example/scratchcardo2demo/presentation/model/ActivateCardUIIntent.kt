package com.example.scratchcardo2demo.presentation.model

sealed interface ActivateCardUIIntent {
    data object Activate : ActivateCardUIIntent
}
