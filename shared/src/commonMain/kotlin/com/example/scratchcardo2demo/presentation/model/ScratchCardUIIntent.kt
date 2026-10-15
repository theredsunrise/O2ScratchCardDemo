package com.example.scratchcardo2demo.presentation.model

sealed interface ScratchCardUiIntent {
    data object Scratch : ScratchCardUiIntent
}