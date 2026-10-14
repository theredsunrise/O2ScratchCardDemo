package com.example.scratchcardo2demo.domain.usecase

import com.example.scratchcardo2demo.domain.`interface`.ScratchCardNetworkRepository
import com.example.scratchcardo2demo.domain.`interface`.ScratchCardPersistenceRepository
import com.example.scratchcardo2demo.domain.model.ScratchCard

interface ActivateCardUseCase {
    suspend operator fun invoke(): ScratchCard
}

class DefaultActivateCardUseCase(
    private val networkRepository: ScratchCardNetworkRepository,
    private val persistenceRepository: ScratchCardPersistenceRepository
) :
    ActivateCardUseCase {

    override suspend fun invoke(): ScratchCard {
        val card =
            persistenceRepository.get()
                ?: throw ActivateCardUseCaseException.CardNotFoundException()
        val version = networkRepository.fetchVersion(cardUuid = card.uuid).android
        if (version <= MIN_VERSION) {
            throw ActivateCardUseCaseException.InsufficientVersionForActivationException(
                version = version
            )
        }

        val updateCard = card.activate(version = version)
        persistenceRepository.update(updateCard)
        return updateCard
    }

    companion object {
        private const val MIN_VERSION = 277028
    }
}