package com.example.scratchcardo2demo.domain.`interface`

import com.example.scratchcardo2demo.domain.model.ScratchCard
import kotlinx.coroutines.flow.Flow

interface ScratchCardPersistenceRepository {
    val state: Flow<ScratchCard?>

    suspend fun update(card: ScratchCard)
    suspend fun get(): ScratchCard?
}