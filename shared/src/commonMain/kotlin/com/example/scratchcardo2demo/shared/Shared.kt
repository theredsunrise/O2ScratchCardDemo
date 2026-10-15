package com.example.scratchcardo2demo.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

expect fun isMainThread(): Boolean

fun assertIsMainThread() = check(isMainThread())
fun assertIsNotMainThread() = check(!isMainThread())

@Composable
fun <T> ObserveAsEvents(
    flow: Flow<T>,
    onEvent: suspend (T) -> Unit,
) {
    val owner = LocalLifecycleOwner.current
    LaunchedEffect(flow) {
        owner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect { onEvent(it) }
            }
        }
    }
}

sealed interface ResultExt<out T> {
    data object Loading : ResultExt<Nothing>
    data class Success<T>(val data: T) : ResultExt<T>
    data class Error(val throwable: Throwable) : ResultExt<Nothing>

    val isLoading: Boolean get() = this is Loading
    val isSuccess: Boolean get() = this is Success
    val asError: Error? get() = this as? Error
}