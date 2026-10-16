package com.example.scratchcardo2demo.presentation.model

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import scratchcardo2demo.shared.generated.resources.Res
import scratchcardo2demo.shared.generated.resources.scratch_card_activate_action
import scratchcardo2demo.shared.generated.resources.scratch_card_activated
import scratchcardo2demo.shared.generated.resources.scratch_card_scratch_action
import scratchcardo2demo.shared.generated.resources.scratch_card_scratched
import scratchcardo2demo.shared.generated.resources.scratch_card_unscratch_action
import scratchcardo2demo.shared.generated.resources.scratch_card_unscratched
import kotlin.uuid.Uuid

enum class ScratchCardUiPhaseId {
    UNSCRATCHED,
    SCRATCHED,
    ACTIVATED;

    @Composable
    fun actionName() = when (this) {
        ACTIVATED -> stringResource(Res.string.scratch_card_activate_action)
        SCRATCHED -> stringResource(Res.string.scratch_card_scratch_action)
        UNSCRATCHED -> stringResource(Res.string.scratch_card_unscratch_action)
    }

    @Composable
    fun statusName() = when (this) {
        ACTIVATED -> stringResource(Res.string.scratch_card_activated)
        SCRATCHED -> stringResource(Res.string.scratch_card_scratched)
        UNSCRATCHED -> stringResource(Res.string.scratch_card_unscratched)
    }

    val isActivated: Boolean get() = this == ACTIVATED
    val isScratched: Boolean get() = this == SCRATCHED
}

data class ScratchCardUiState(
    val loadingAndError: ScratchCardUiLoadingAndError,
    val phase: ScratchCardUiPhase
) {
    val uuid: Uuid?
        get() = when (phase) {
            is ScratchCardUiPhase.Activated -> phase.cardUuid
            is ScratchCardUiPhase.Scratched -> phase.cardUuid
            is ScratchCardUiPhase.Unscratched -> null
        }

    fun isCurrentPhaseBelow(phaseId: ScratchCardUiPhaseId): Boolean {
        return when {
            phase.isUnscratched && phaseId.isScratched -> true
            phase.isScratched && phaseId.isActivated -> true
            else -> false
        }
    }

    companion object {
        val UNSCRATCHED = ScratchCardUiState(
            loadingAndError = ScratchCardUiLoadingAndError.Nothing,
            phase = ScratchCardUiPhase.Unscratched
        )
    }
}