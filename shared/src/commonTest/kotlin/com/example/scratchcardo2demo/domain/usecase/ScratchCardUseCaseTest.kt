package com.example.scratchcardo2demo.domain.usecase

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

class ScratchCardUseCaseTest {

    @Test
    fun `scratches and persists existing card`() = runTest {
        val card = ScratchCard.createNew()
        val scratchedCard = card.scratch()
        val repository = mock<ScratchCardPersistenceRepository>(autoUnit)
        everySuspend { repository.get() } returns card

        val result = DefaultScratchCardUseCase(repository).invoke()

        result shouldBe scratchedCard
        verifySuspend { repository.update(scratchedCard) }
    }

    @Test
    fun `creates and persists a new card when repository is empty`() = runTest {
        val repository = mock<ScratchCardPersistenceRepository>(autoUnit)
        everySuspend { repository.get() } returns null

        val result = DefaultScratchCardUseCase(repository).invoke()

        result.phase shouldBe ScratchCard.ScratchCardPhase.Scratched
        verifySuspend { repository.update(result) }
    }
}
