package com.example.scratchcardo2demo.presentation.viewmodel

import app.cash.turbine.test
import com.example.scratchcardo2demo.domain.`interface`.ScratchCardActivatorManager
import com.example.scratchcardo2demo.domain.model.ScratchCard
import com.example.scratchcardo2demo.domain.usecase.ActivateCardUseCaseException
import com.example.scratchcardo2demo.presentation.mapper.PresentationError
import com.example.scratchcardo2demo.presentation.model.ActivateCardUiIntent
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiPhase
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiLoadingAndError
import dev.mokkery.MockMode.autoUnit
import dev.mokkery.answering.calls
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.answering.returns
import dev.mokkery.verify
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import com.example.scratchcardo2demo.shared.ResultExt
import scratchcardo2demo.shared.generated.resources.Res
import scratchcardo2demo.shared.generated.resources.error_insufficient_version

@OptIn(ExperimentalCoroutinesApi::class)
class ActivateCardScreenViewModelTest {

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
    fun `emits loading and then activated state`() = runTest {
        val scratchedCard = ScratchCard.createNew().scratch()
        val activatedCard = scratchedCard.activate(287028)
        val (repository, repositoryState) = mockPersistenceRepository(scratchedCard)
        val manager = mock<ScratchCardActivatorManager>(autoUnit)
        val managerState = MutableSharedFlow<ResultExt<ScratchCard>?>(
            replay = 1,
            extraBufferCapacity = 8,
        )
        every { manager.state } returns managerState
        every { manager.activate() } calls { managerState.tryEmit(ResultExt.Loading) }
        val viewModel = ActivateCardScreenViewModel(testDispatcher, manager, repository)

        viewModel.uiState.test {
            awaitItem()
            viewModel.intent(ActivateCardUiIntent.Activate)
            awaitItem().loadingAndError shouldBe ScratchCardUiLoadingAndError.Loading

            repositoryState.value = activatedCard
            val activatingState = awaitItem()
            activatingState.phase shouldBe ScratchCardUiPhase.Activated(activatedCard.uuid, 287028)
            activatingState.loadingAndError shouldBe ScratchCardUiLoadingAndError.Loading
            managerState.tryEmit(ResultExt.Success(activatedCard))

            val activatedState = awaitItem()
            activatedState.phase shouldBe ScratchCardUiPhase.Activated(activatedCard.uuid, 287028)
            activatedState.loadingAndError shouldBe ScratchCardUiLoadingAndError.Nothing
            verify { manager.activate() }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits presentation error`() = runTest {
        val (repository, _) = mockPersistenceRepository(ScratchCard.createNew().scratch())
        val manager = mock<ScratchCardActivatorManager>(autoUnit)
        val managerState = MutableSharedFlow<ResultExt<ScratchCard>?>(
            replay = 1,
            extraBufferCapacity = 8,
        )
        every { manager.state } returns managerState
        every { manager.activate() } calls { managerState.tryEmit(ResultExt.Loading) }
        val viewModel = ActivateCardScreenViewModel(testDispatcher, manager, repository)

        viewModel.uiState.test {
            awaitItem()
            viewModel.intent(ActivateCardUiIntent.Activate)
            awaitItem().loadingAndError shouldBe ScratchCardUiLoadingAndError.Loading
            managerState.tryEmit(ResultExt.Error(IllegalStateException("activation failed")))

            val state = awaitItem()
            (state.loadingAndError as ScratchCardUiLoadingAndError.Error).error shouldBe
                PresentationError.Text("activation failed")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `maps insufficient version error to presentation resource`() = runTest {
        val (repository, _) = mockPersistenceRepository(ScratchCard.createNew().scratch())
        val manager = mock<ScratchCardActivatorManager>(autoUnit)
        val managerState = MutableSharedFlow<ResultExt<ScratchCard>?>(
            replay = 1,
            extraBufferCapacity = 8,
        )
        every { manager.state } returns managerState
        every { manager.activate() } calls { managerState.tryEmit(ResultExt.Loading) }
        val viewModel = ActivateCardScreenViewModel(testDispatcher, manager, repository)

        viewModel.uiState.test {
            awaitItem()
            viewModel.intent(ActivateCardUiIntent.Activate)
            awaitItem().loadingAndError shouldBe ScratchCardUiLoadingAndError.Loading
            managerState.tryEmit(
                ResultExt.Error(
                    ActivateCardUseCaseException.InsufficientVersionForActivationException(
                        version = 277028,
                    ),
                ),
            )

            val state = awaitItem()
            (state.loadingAndError as ScratchCardUiLoadingAndError.Error).error shouldBe
                PresentationError.Resource(Res.string.error_insufficient_version)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
