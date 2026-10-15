package com.example.scratchcardo2demo.domain.usecase

import com.example.scratchcardo2demo.data.networking.model.Version
import com.example.scratchcardo2demo.domain.`interface`.ScratchCardNetworkRepository
import com.example.scratchcardo2demo.domain.`interface`.ScratchCardPersistenceRepository
import com.example.scratchcardo2demo.domain.model.ScratchCard
import dev.mokkery.MockMode.autoUnit
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class ActivateCardUseCaseTest {

    @Test
    fun `activates and persists card when version is sufficient`() = runTest {
        val card = ScratchCard.createNew().scratch()
        val version = 287028
        val repository = mock<ScratchCardPersistenceRepository>(autoUnit)
        val networkRepository = mock<ScratchCardNetworkRepository>(autoUnit)
        everySuspend { repository.get() } returns card
        everySuspend { networkRepository.fetchVersion(card.uuid) } returns Version(version)

        val result = DefaultActivateCardUseCase(networkRepository, repository).invoke()

        result.version shouldBe version
        verifySuspend { repository.update(result) }
    }

    @Test
    fun `fails when card is missing`() = runTest {
        val repository = mock<ScratchCardPersistenceRepository>(autoUnit)
        val networkRepository = mock<ScratchCardNetworkRepository>(autoUnit)
        everySuspend { repository.get() } returns null

        assertFailsWith<ActivateCardUseCaseException.CardNotFoundException> {
            DefaultActivateCardUseCase(networkRepository, repository).invoke()
        }
    }

    @Test
    fun `fails when version is not sufficient`() = runTest {
        val card = ScratchCard.createNew().scratch()
        val version = 277028
        val repository = mock<ScratchCardPersistenceRepository>(autoUnit)
        val networkRepository = mock<ScratchCardNetworkRepository>(autoUnit)
        everySuspend { repository.get() } returns card
        everySuspend { networkRepository.fetchVersion(card.uuid) } returns Version(version)

        val exception = assertFailsWith<ActivateCardUseCaseException.InsufficientVersionForActivationException> {
            DefaultActivateCardUseCase(networkRepository, repository).invoke()
        }

        exception.version shouldBe version
    }
}
