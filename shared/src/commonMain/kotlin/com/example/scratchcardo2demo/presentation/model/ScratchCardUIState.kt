package com.example.scratchcardo2demo.presentation.model

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import scratchcardo2demo.shared.generated.resources.Res
import scratchcardo2demo.shared.generated.resources.scratch_card_activated
import scratchcardo2demo.shared.generated.resources.scratch_card_scratched
import scratchcardo2demo.shared.generated.resources.scratch_card_unscratched
import kotlin.uuid.Uuid

enum class ScratchCardUIPhaseId {
    UNSCRATCHED,
    SCRATCHED,
    ACTIVATED;

    @Composable
    fun name() = when (this) {
        ACTIVATED -> stringResource(Res.string.scratch_card_activated)
        SCRATCHED -> stringResource(Res.string.scratch_card_scratched)
        UNSCRATCHED -> stringResource(Res.string.scratch_card_unscratched)
    }

    val isActivated: Boolean get() = this == ACTIVATED
    val isScratched: Boolean get() = this == SCRATCHED
    val isUnscratched: Boolean get() = this == UNSCRATCHED
}

sealed class ScratchCardUIPhase(val id: ScratchCardUIPhaseId) {
    data object Unscratched : ScratchCardUIPhase(id = ScratchCardUIPhaseId.UNSCRATCHED)
    data class Scratched(val cardUuid: Uuid) :
        ScratchCardUIPhase(id = ScratchCardUIPhaseId.SCRATCHED)

    data class Activated(val cardUuid: Uuid, val version: Int) :
        ScratchCardUIPhase(id = ScratchCardUIPhaseId.ACTIVATED)

    val isScratched: Boolean get() = this is Scratched
    val isActivated: Boolean get() = this is Activated
    val isUnscratched: Boolean get() = this is Unscratched
}

sealed interface ScratchCardUIState {
    data object Loading : ScratchCardUIState
    data class Success(val phase: ScratchCardUIPhase) : ScratchCardUIState {
        val uuid: Uuid?
            get() = when (phase) {
                is ScratchCardUIPhase.Activated -> phase.cardUuid
                is ScratchCardUIPhase.Scratched -> phase.cardUuid
                is ScratchCardUIPhase.Unscratched -> null
            }
    }

    data class Error(val error: Throwable) : ScratchCardUIState

    val isSuccess: Boolean get() = this is Success
    val isLoading: Boolean get() = this is Loading
    val isError: Boolean get() = this is Error
    val asSuccess: Success? get() = this as? Success
    fun isCurrentPhaseBelow(phaseId: ScratchCardUIPhaseId): Boolean {
        val currentPhase = asSuccess?.phase ?: return false
        return when {
            currentPhase.isUnscratched && phaseId.isScratched -> true
            currentPhase.isScratched && phaseId.isActivated -> true
            else -> false
        }
    }
}