package com.example.scratchcardo2demo.presentation.mapper

import com.example.scratchcardo2demo.domain.model.ScratchCard
import com.example.scratchcardo2demo.presentation.model.ScratchCardUIPhase
import com.example.scratchcardo2demo.presentation.model.ScratchCardUIState

fun ScratchCard.toUI(): ScratchCardUIState = when (phase) {
    is ScratchCard.ScratchCardPhase.UnScratched ->
        ScratchCardUIState.Success(phase = ScratchCardUIPhase.Unscratched)

    is ScratchCard.ScratchCardPhase.Scratched ->
        ScratchCardUIState.Success(phase = ScratchCardUIPhase.Scratched(uuid))

    is ScratchCard.ScratchCardPhase.Activated ->
        ScratchCardUIState.Success(phase = ScratchCardUIPhase.Activated(uuid, phase.version))
}
