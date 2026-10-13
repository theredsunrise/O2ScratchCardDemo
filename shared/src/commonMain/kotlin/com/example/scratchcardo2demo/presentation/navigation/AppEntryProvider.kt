package com.example.scratchcardo2demo.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import com.example.scratchcardo2demo.presentation.screen.ScratchCardScreen

fun appEntryProvider(navigator: Navigator): (NavKey) -> NavEntry<NavKey> = entryProvider {

    entry<AppDestination.ScratchCard> {
        ScratchCardScreen(modifier = Modifier.fillMaxSize())
    }

}