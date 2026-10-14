package com.example.scratchcardo2demo.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.ui.NavDisplay
import com.example.scratchcardo2demo.presentation.navigation.AppDestination
import com.example.scratchcardo2demo.presentation.navigation.appEntryProvider
import com.example.scratchcardo2demo.presentation.navigation.rememberNavigator
import com.example.scratchcardo2demo.presentation.navigation.toEntries

@Composable
fun App() {
    MaterialTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
            val navigator = rememberNavigator(
                startDestination = AppDestination.AppScreen,
                topLevelDestinations = setOf(AppDestination.AppScreen)
            )
            NavDisplay(
                modifier = Modifier.fillMaxSize().padding(paddingValues = paddingValues),
                entries = navigator.navigationState.toEntries(appEntryProvider(navigator)),
                transitionSpec = defaultTransitionSpec(),
                popTransitionSpec = defaultPopTransitionSpec(),
                predictivePopTransitionSpec = defaultPredictivePopTransitionSpec(),
                onBack = { navigator.goBack() }
            )
        }
    }
}

@Preview
@Composable
private fun AppPreview() {
    App()
}