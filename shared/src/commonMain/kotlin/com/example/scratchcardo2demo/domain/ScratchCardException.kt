package com.example.scratchcardo2demo.domain

sealed class ScratchCardException : Exception() {
    class InvalidStateTransitionException(
        val from: ScratchCard.ScratchCardState,
        val to: ScratchCard.ScratchCardState
    ) : ScratchCardException()
}