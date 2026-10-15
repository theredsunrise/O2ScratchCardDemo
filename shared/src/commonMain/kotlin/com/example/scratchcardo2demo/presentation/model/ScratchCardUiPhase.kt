package com.example.scratchcardo2demo.presentation.model

import kotlin.uuid.Uuid

sealed class ScratchCardUiPhase(val id: ScratchCardUiPhaseId) {
    data object Unscratched : ScratchCardUiPhase(id = ScratchCardUiPhaseId.UNSCRATCHED)
    data class Scratched(val cardUuid: Uuid) :
        ScratchCardUiPhase(id = ScratchCardUiPhaseId.SCRATCHED)

    data class Activated(val cardUuid: Uuid, val version: Int) :
        ScratchCardUiPhase(id = ScratchCardUiPhaseId.ACTIVATED)

    val isScratched: Boolean get() = this is Scratched
    val isUnscratched: Boolean get() = this is Unscratched
}