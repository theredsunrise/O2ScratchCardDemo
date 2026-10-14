package com.example.scratchcardo2demo.domain.model

import com.example.scratchcardo2demo.domain.model.ScratchCard.ScratchCardPhaseId

sealed class ScratchCardException : Exception() {
    data class InvalidStateTransitionException(
        val from: ScratchCardPhaseId,
        val to: ScratchCardPhaseId
    ) : ScratchCardException()
}