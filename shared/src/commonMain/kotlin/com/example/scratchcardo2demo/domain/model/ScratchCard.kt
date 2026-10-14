package com.example.scratchcardo2demo.domain.model

import kotlin.uuid.Uuid

data class ScratchCard(
    val uuid: Uuid,
    val phase: ScratchCardPhase = ScratchCardPhase.UnScratched,
) {
    enum class ScratchCardPhaseId {
        UNSCRATCHED,
        SCRATCHED,
        ACTIVATED;

        val isScratched: Boolean get() = this == SCRATCHED
        val isActivated: Boolean get() = this == ACTIVATED
        val isUnscratched: Boolean get() = this == UNSCRATCHED
    }

    sealed class ScratchCardPhase(val id: ScratchCardPhaseId) {
        data object UnScratched : ScratchCardPhase(id = ScratchCardPhaseId.UNSCRATCHED)
        data object Scratched : ScratchCardPhase(id = ScratchCardPhaseId.SCRATCHED)
        data class Activated(val version: Int) : ScratchCardPhase(id = ScratchCardPhaseId.ACTIVATED)
    }

    val version: Int? get() = (phase as? ScratchCardPhase.Activated)?.version

    fun scratch(): ScratchCard =
        when (phase) {
            is ScratchCardPhase.UnScratched ->
                copy(phase = ScratchCardPhase.Scratched)

            is ScratchCardPhase.Scratched -> this

            else -> throw ScratchCardException.InvalidStateTransitionException(
                phase.id,
                ScratchCardPhaseId.SCRATCHED,
            )
        }

    fun activate(version: Int): ScratchCard =
        when (phase) {
            is ScratchCardPhase.Scratched ->
                copy(
                    phase = ScratchCardPhase.Activated(version = version),
                )

            is ScratchCardPhase.Activated -> this

            else -> throw ScratchCardException.InvalidStateTransitionException(
                phase.id,
                ScratchCardPhaseId.ACTIVATED,
            )
        }

    companion object {
        fun createNew(): ScratchCard = ScratchCard(Uuid.random())
    }
}