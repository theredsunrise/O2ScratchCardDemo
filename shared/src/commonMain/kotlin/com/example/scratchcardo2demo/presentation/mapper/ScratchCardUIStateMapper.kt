package com.example.scratchcardo2demo.presentation.mapper

import com.example.scratchcardo2demo.domain.model.ScratchCard
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiPhase
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiResult
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiState
import com.example.scratchcardo2demo.shared.ResultExt

fun ScratchCard.toUi(result: ScratchCardUiResult): ScratchCardUiState =
    ScratchCardUiState(
        uiResult = result,
        currentPhase = when (this.phase) {
            is ScratchCard.ScratchCardPhase.UnScratched ->
                ScratchCardUiPhase.Unscratched

            is ScratchCard.ScratchCardPhase.Scratched ->
                ScratchCardUiPhase.Scratched(cardUuid = this.uuid)

            is ScratchCard.ScratchCardPhase.Activated ->
                ScratchCardUiPhase.Activated(cardUuid = this.uuid, version = phase.version)
        }
    )

fun ResultExt<ScratchCard>.toUiResult(): ScratchCardUiResult = when (this) {
    is ResultExt.Error -> ScratchCardUiResult.Error(throwable.toPresentationError())
    is ResultExt.Loading -> ScratchCardUiResult.Loading
    is ResultExt.Success -> ScratchCardUiResult.Nothing
}
