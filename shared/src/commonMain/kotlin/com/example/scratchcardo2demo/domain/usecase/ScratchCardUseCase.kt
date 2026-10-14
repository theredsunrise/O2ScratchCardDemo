package com.example.scratchcardo2demo.domain.usecase

import com.example.scratchcardo2demo.domain.`interface`.ScratchCardPersistenceRepository
import com.example.scratchcardo2demo.domain.model.ScratchCard
import kotlinx.coroutines.delay

interface ScratchCardUseCase {
    suspend operator fun invoke(): ScratchCard
}

class DefaultScratchCardUseCase(
    private val scratchCardRepository: ScratchCardPersistenceRepository
) :
    ScratchCardUseCase {

    override suspend fun invoke(): ScratchCard {
        val card = scratchCardRepository.get() ?: ScratchCard.createNew()
        delay(2000)
        val updatedCard = card.scratch()
        scratchCardRepository.update(updatedCard)
        return updatedCard
    }
}