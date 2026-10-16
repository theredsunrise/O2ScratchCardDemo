package com.example.scratchcardo2demo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class Navigator(val navigationState: NavigationState) {
    fun navigateTo(destination: AppDestination) =
        navigationState.apply {
            if (destination in topLevelDestinations) {
                currentTopLevelDestination = destination
            } else {
                if(currentBackStack.lastOrNull() != destination) {
                    currentBackStack.add(destination)
                }
            }
        }

    fun goBack() =
        navigationState.currentBackStack.apply {
            if (size > 1) {
                removeAt(lastIndex)
            }
        }
}

@Composable
fun rememberNavigator(
    startDestination: AppDestination,
    topLevelDestinations: Set<AppDestination>
): Navigator {
    val navigationState = rememberNavigationState(startDestination, topLevelDestinations)
    return remember(startDestination, topLevelDestinations) {
        Navigator(navigationState = navigationState)
    }
}
