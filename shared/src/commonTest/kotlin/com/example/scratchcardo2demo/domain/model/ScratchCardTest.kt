package com.example.scratchcardo2demo.domain.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ScratchCardTest {

    @Test
    fun `new card starts unscratched`() {
        val card = ScratchCard.createNew()

        card.phase shouldBe ScratchCard.ScratchCardPhase.UnScratched
        card.version shouldBe null
    }

    @Test
    fun `scratching changes card to scratched`() {
        val card = ScratchCard.createNew()

        val scratchedCard = card.scratch()

        scratchedCard.uuid shouldBe card.uuid
        scratchedCard.phase shouldBe ScratchCard.ScratchCardPhase.Scratched
    }

    @Test
    fun `scratching an already scratched card keeps its state`() {
        val scratchedCard = ScratchCard.createNew().scratch()

        scratchedCard.scratch() shouldBe scratchedCard
    }

    @Test
    fun `scratching an activated card is invalid`() {
        val activatedCard = ScratchCard.createNew()
            .scratch()
            .activate(version = 287028)

        shouldThrow<ScratchCardException.InvalidStateTransitionException> {
            activatedCard.scratch()
        }
    }

    @Test
    fun `activating a scratched card stores the version`() {
        val scratchedCard = ScratchCard.createNew().scratch()

        val activatedCard = scratchedCard.activate(version = 287028)

        activatedCard.uuid shouldBe scratchedCard.uuid
        activatedCard.phase shouldBe ScratchCard.ScratchCardPhase.Activated(287028)
        activatedCard.version shouldBe 287028
    }

    @Test
    fun `activating an already activated card keeps its original version`() {
        val activatedCard = ScratchCard.createNew()
            .scratch()
            .activate(version = 287028)

        activatedCard.activate(version = 300000) shouldBe activatedCard
    }

    @Test
    fun `activating an unscratched card is invalid`() {
        shouldThrow<ScratchCardException.InvalidStateTransitionException> {
            ScratchCard.createNew().activate(version = 287028)
        }
    }
}
