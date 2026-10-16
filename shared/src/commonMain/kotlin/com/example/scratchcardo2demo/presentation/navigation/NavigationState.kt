package com.example.scratchcardo2demo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator

class NavigationState(
    val startTopLevelDestinationState: MutableState<AppDestination>,
    val topLevelDestinations: Set<NavKey>,
    val backStackMap: Map<NavKey, NavBackStack<NavKey>>
) {
    var currentTopLevelDestination by startTopLevelDestinationState
    val currentBackStack: NavBackStack<NavKey>
        get() = requireNotNull(backStackMap[currentTopLevelDestination]) {
            "No backstack for destination $currentTopLevelDestination"
        }

    init {
        check(startTopLevelDestinationState.value in topLevelDestinations)
        check(backStackMap.keys == topLevelDestinations)
    }
}

@Composable
//prepared to support additional top-level app destinations when tabs are introduced.
fun NavigationState.toEntries(entryProvider: (NavKey) -> NavEntry<NavKey>): SnapshotStateList<NavEntry<NavKey>> {
    val decoratedBackStackMap = backStackMap.mapValues { (_, stack) ->
        val decorators: List<NavEntryDecorator<NavKey>> = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        )
        rememberDecoratedNavEntries(stack, decorators, entryProvider)
    }
    return requireNotNull(decoratedBackStackMap[currentTopLevelDestination]) {
        "No backstack for destination $currentTopLevelDestination"
    }.toMutableStateList()
}

@Composable
fun rememberNavigationState(
    startDestination: AppDestination,
    topLevelDestinations: Set<AppDestination>
): NavigationState {
    val backStackMap: Map<NavKey, NavBackStack<NavKey>> = topLevelDestinations.associateWith {
        rememberNavBackStack(configuration = AppDestination.SAVED_STATE_CONFIG, it)
    }

    val startTopLevelDestinationState =
        rememberSerializable(startDestination, AppDestination.SAVED_STATE_CONFIG) {
            mutableStateOf(startDestination)
        }

    return remember(startDestination, topLevelDestinations) {
        NavigationState(
            startTopLevelDestinationState = startTopLevelDestinationState,
            topLevelDestinations = topLevelDestinations,
            backStackMap = backStackMap
        )
    }
}