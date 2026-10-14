package com.example.scratchcardo2demo.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import com.example.scratchcardo2demo.presentation.screen.ActivateCardScreen
import com.example.scratchcardo2demo.presentation.screen.AppScreen
import com.example.scratchcardo2demo.presentation.screen.ScratchCardScreen
import com.example.scratchcardo2demo.presentation.viewmodel.ActivateCardScreenViewModel
import com.example.scratchcardo2demo.presentation.viewmodel.AppViewModel
import com.example.scratchcardo2demo.presentation.viewmodel.ScratchCardScreenViewModel
import org.koin.compose.viewmodel.koinViewModel

fun appEntryProvider(navigator: Navigator): (NavKey) -> NavEntry<NavKey> = entryProvider {

    entry<AppDestination.AppScreen> {
        val viewModel = koinViewModel<AppViewModel>()
        AppScreen(
            modifier = Modifier.fillMaxSize(),
            uiState = viewModel.uiState.collectAsStateWithLifecycle(),
        ) {
            navigator.navigateTo(it)
        }
    }
    entry<AppDestination.ScratchCard> {
        val viewModel = koinViewModel<ScratchCardScreenViewModel>()
        ScratchCardScreen(modifier = Modifier.fillMaxSize(),
            uiState = viewModel.uiState.collectAsStateWithLifecycle()) {
            viewModel.intent(it)
        }
    }
    entry<AppDestination.ActivateCard> {
        val viewModel = koinViewModel<ActivateCardScreenViewModel>()
        ActivateCardScreen(modifier = Modifier.fillMaxSize(),
            uiState = viewModel.uiState.collectAsStateWithLifecycle()) {
            viewModel.intent(it)
        }
    }

}