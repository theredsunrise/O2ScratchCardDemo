package com.example.scratchcardo2demo.domain.`interface`

import com.example.scratchcardo2demo.data.networking.model.Version
import kotlin.uuid.Uuid

interface ScratchCardNetworkRepository {
    suspend fun fetchVersion(cardUuid: Uuid): Version
}