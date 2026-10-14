package com.example.scratchcardo2demo.data.persistence.mapper

import com.example.scratchcardo2demo.data.persistence.model.ScratchCardDto
import com.example.scratchcardo2demo.domain.model.ScratchCard

fun ScratchCardDto.toDomain(): ScratchCard = when (this.state) {
    ScratchCardDto.ScratchCardState.UNSCRATCHED -> ScratchCard(
        uuid = uuid,
        phase = ScratchCard.ScratchCardPhase.UnScratched
    )

    ScratchCardDto.ScratchCardState.SCRATCHED -> ScratchCard(
        uuid = uuid,
        phase = ScratchCard.ScratchCardPhase.Scratched
    )

    ScratchCardDto.ScratchCardState.ACTIVATED -> {
        requireNotNull(version) { "Version must not be null when state is Activated." }
        ScratchCard(
            uuid = uuid,
            phase = ScratchCard.ScratchCardPhase.Activated(version = version)
        )
    }
}

fun ScratchCard.toDto(): ScratchCardDto =
    ScratchCardDto(
        uuid = uuid,
        version = version,
        state = when (phase) {
            is ScratchCard.ScratchCardPhase.UnScratched -> ScratchCardDto.ScratchCardState.UNSCRATCHED
            is ScratchCard.ScratchCardPhase.Scratched -> ScratchCardDto.ScratchCardState.SCRATCHED
            is ScratchCard.ScratchCardPhase.Activated -> ScratchCardDto.ScratchCardState.ACTIVATED
        }
    )