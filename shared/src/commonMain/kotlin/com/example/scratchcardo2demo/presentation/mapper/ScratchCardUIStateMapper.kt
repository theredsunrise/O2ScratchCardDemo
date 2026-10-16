package com.example.scratchcardo2demo.presentation.mapper

import com.example.scratchcardo2demo.domain.model.ScratchCard
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiPhase
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiLoadingAndError
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiState
import com.example.scratchcardo2demo.shared.ResultExt

fun ScratchCard.toUi(loadingAndError: ScratchCardUiLoadingAndError): ScratchCardUiState =
    ScratchCardUiState(
        loadingAndError = loadingAndError,
        phase = when (this.phase) {
            is ScratchCard.ScratchCardPhase.UnScratched ->
                ScratchCardUiPhase.Unscratched

            is ScratchCard.ScratchCardPhase.Scratched ->
                ScratchCardUiPhase.Scratched(cardUuid = this.uuid)

            is ScratchCard.ScratchCardPhase.Activated ->
                ScratchCardUiPhase.Activated(cardUuid = this.uuid, version = phase.version)
        }
    )

fun ResultExt<ScratchCard>.toLoadingAndError(): ScratchCardUiLoadingAndError = when (this) {
    is ResultExt.Error -> ScratchCardUiLoadingAndError.Error(throwable.toPresentationError())
    is ResultExt.Loading -> ScratchCardUiLoadingAndError.Loading
    is ResultExt.Success -> ScratchCardUiLoadingAndError.Nothing
}
