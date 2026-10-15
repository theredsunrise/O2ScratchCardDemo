package com.example.scratchcardo2demo.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.ui.NavDisplay
import com.example.scratchcardo2demo.presentation.navigation.AppDestination
import com.example.scratchcardo2demo.presentation.navigation.appEntryProvider
import com.example.scratchcardo2demo.presentation.navigation.rememberNavigator
import com.example.scratchcardo2demo.presentation.navigation.toEntries
import com.example.scratchcardo2demo.presentation.mapper.PresentationError
import com.example.scratchcardo2demo.presentation.screen.component.ModalDialog
import com.example.scratchcardo2demo.shared.ObserveAsEvents
import org.koin.compose.koinInject
import scratchcardo2demo.shared.generated.resources.Res
import scratchcardo2demo.shared.generated.resources.error_title

@Composable
fun App() {
    MaterialTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
            val navigator = rememberNavigator(
                startDestination = AppDestination.AppScreen,
                topLevelDestinations = setOf(AppDestination.AppScreen)
            )

            val errorState = remember { mutableStateOf<PresentationError?>(null) }
            val appState = koinInject<AppState>()
            ObserveAsEvents(flow = appState.errorFlow) {
                errorState.value = it
            }

            NavDisplay(
                modifier = Modifier.fillMaxSize().padding(paddingValues = paddingValues),
                entries = navigator.navigationState.toEntries(appEntryProvider(navigator)),
                transitionSpec = defaultTransitionSpec(),
                popTransitionSpec = defaultPopTransitionSpec(),
                predictivePopTransitionSpec = defaultPredictivePopTransitionSpec(),
                onBack = { navigator.goBack() }
            )
            ModalDialog(
                title = Res.string.error_title,
                messageState = errorState
            ) {
                errorState.value = null
            }
        }
    }
}

@Preview
@Composable
private fun AppPreview() {
    App()
}
