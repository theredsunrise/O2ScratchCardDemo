package com.example.scratchcardo2demo.data.persistence.mapper

import com.example.scratchcardo2demo.data.persistence.model.ScratchCardDto
import com.example.scratchcardo2demo.domain.ScratchCard

fun ScratchCardDto.ScratchCardState.toDomain(): ScratchCard.ScratchCardState =
    when (this) {
        ScratchCardDto.ScratchCardState.UNSCRATCHED -> ScratchCard.ScratchCardState.UNSCRATCHED
        ScratchCardDto.ScratchCardState.SCRATCHED -> ScratchCard.ScratchCardState.SCRATCHED
        ScratchCardDto.ScratchCardState.ACTIVATED -> ScratchCard.ScratchCardState.ACTIVATED
    }

fun ScratchCard.ScratchCardState.toDto(): ScratchCardDto.ScratchCardState =
    when (this) {
        ScratchCard.ScratchCardState.UNSCRATCHED -> ScratchCardDto.ScratchCardState.UNSCRATCHED
        ScratchCard.ScratchCardState.SCRATCHED -> ScratchCardDto.ScratchCardState.SCRATCHED
        ScratchCard.ScratchCardState.ACTIVATED -> ScratchCardDto.ScratchCardState.ACTIVATED
    }

fun ScratchCardDto.toDomain(): ScratchCard =
    ScratchCard(
        uuid = this.uuid,
        internalState = this.internalState,
        state = this.state.toDomain()
    )

fun ScratchCard.toDto(): ScratchCardDto =
    ScratchCardDto(
        uuid = this.uuid,
        internalState = this.internalState,
        state = this.state.toDto()
    )