package com.example.scratchcardo2demo.domain.`interface`

import com.example.scratchcardo2demo.domain.ScratchCard

interface ScratchCardPersistenceRepository {
    suspend fun get(): ScratchCard?
    suspend fun update(card: ScratchCard)
}