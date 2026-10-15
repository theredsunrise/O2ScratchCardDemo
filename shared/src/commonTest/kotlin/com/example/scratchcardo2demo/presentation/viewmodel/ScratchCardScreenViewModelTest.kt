package com.example.scratchcardo2demo.presentation.viewmodel

import app.cash.turbine.test
import com.example.scratchcardo2demo.domain.model.ScratchCard
import com.example.scratchcardo2demo.domain.usecase.ScratchCardUseCase
import com.example.scratchcardo2demo.presentation.mapper.PresentationError
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiIntent
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiPhase
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiResult
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiState
import dev.mokkery.MockMode.autoUnit
import dev.mokkery.answering.calls
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ScratchCardScreenViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `emits loading and then scratched state`() = runTest {
        val scratchedCard = ScratchCard.createNew().scratch()
        val (repository, repositoryState) = mockPersistenceRepository()
        repositoryState.value = scratchedCard
        val useCase = mock<ScratchCardUseCase>(autoUnit)
        everySuspend { useCase() } returns scratchedCard
        val viewModel = ScratchCardScreenViewModel(testDispatcher, useCase, repository)

        viewModel.uiState.test {
            val initialState = awaitItem()
            initialState.currentPhase shouldBe ScratchCardUiPhase.Scratched(scratchedCard.uuid)
            initialState.uiResult shouldBe ScratchCardUiResult.Nothing
            viewModel.intent(ScratchCardUiIntent.Scratch)
            awaitItem().uiResult shouldBe ScratchCardUiResult.Loading

            val result = awaitItem()
            result.currentPhase shouldBe ScratchCardUiPhase.Scratched(scratchedCard.uuid)
            result.uiResult shouldBe ScratchCardUiResult.Nothing
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits presentation error`() = runTest {
        val failure = IllegalStateException("scratch failed")
        val useCase = mock<ScratchCardUseCase>(autoUnit)
        everySuspend { useCase() } calls { throw failure }
        val (repository, _) = mockPersistenceRepository()
        val viewModel = ScratchCardScreenViewModel(testDispatcher, useCase, repository)

        viewModel.uiState.test {
            awaitItem()
            viewModel.intent(ScratchCardUiIntent.Scratch)
            awaitItem().uiResult shouldBe ScratchCardUiResult.Loading

            val state = awaitItem()
            (state.uiResult as ScratchCardUiResult.Error).error shouldBe
                PresentationError.Text("scratch failed")
            cancelAndIgnoreRemainingEvents()
        }
    }
}
