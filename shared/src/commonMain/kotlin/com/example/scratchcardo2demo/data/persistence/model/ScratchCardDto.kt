package com.example.scratchcardo2demo.data.persistence.model

import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class ScratchCardDto(
    val uuid: Uuid,
    val version: Int?,
    val state: ScratchCardState
) {
    @Serializable
    enum class ScratchCardState {
        UNSCRATCHED,
        SCRATCHED,
        ACTIVATED
    }
}