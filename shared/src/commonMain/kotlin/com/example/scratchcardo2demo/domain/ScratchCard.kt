package com.example.scratchcardo2demo.domain

import kotlin.uuid.Uuid

class ScratchCard(
    uuid: Uuid? = null,
    internalState: Int? = null,
    state: ScratchCardState = ScratchCardState.UNSCRATCHED
) {
    enum class ScratchCardState {
        UNSCRATCHED,
        SCRATCHED,
        ACTIVATED
    }

    var uuid: Uuid? = uuid
        private set

    var internalState: Int? = internalState
        private set

    var state: ScratchCardState = state
        private set

    fun scratch(uuid: Uuid) {
        when (state) {
            ScratchCardState.UNSCRATCHED -> {
                this.uuid = uuid
                this.state = ScratchCardState.SCRATCHED
            }

            else -> throw ScratchCardException.InvalidStateTransitionException(
                state,
                ScratchCardState.SCRATCHED,
            )
        }
    }

    fun activate(internalState: Int) {
        when (state) {
            ScratchCardState.SCRATCHED -> {
                this.internalState = internalState
                this.state = ScratchCardState.ACTIVATED
            }

            else -> throw ScratchCardException.InvalidStateTransitionException(
                state,
                ScratchCardState.ACTIVATED
            )
        }
    }
}