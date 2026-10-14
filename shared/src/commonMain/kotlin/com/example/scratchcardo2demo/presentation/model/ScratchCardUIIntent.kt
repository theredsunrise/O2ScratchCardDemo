package com.example.scratchcardo2demo.presentation.model

sealed interface ScratchCardUIIntent {
    data object Scratch : ScratchCardUIIntent
}