package com.example.scratchcardo2demo.presentation.viewmodel

import com.example.scratchcardo2demo.domain.`interface`.ScratchCardPersistenceRepository
import com.example.scratchcardo2demo.domain.model.ScratchCard
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiPhase
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiState
import dev.mokkery.MockMode.autoUnit
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.answering.returns
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import app.cash.turbine.test
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AppViewModelTest {

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
    fun `emits the current persisted state`() = runTest {
        val scratchedCard = ScratchCard.createNew().scratch()
        val repository = mock<ScratchCardPersistenceRepository>(autoUnit)
        val repositoryState = MutableStateFlow<ScratchCard?>(null)
        every { repository.state } returns repositoryState
        val viewModel = AppViewModel(testDispatcher, repository)

        viewModel.uiState.test {
            awaitItem() shouldBe ScratchCardUiState.UNSCRATCHED
            repositoryState.value = scratchedCard

            awaitItem().currentPhase shouldBe ScratchCardUiPhase.Scratched(scratchedCard.uuid)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
