package com.example.scratchcardo2demo.presentation

import com.example.scratchcardo2demo.domain.`interface`.ScratchCardActivatorManager
import com.example.scratchcardo2demo.presentation.mapper.PresentationError
import com.example.scratchcardo2demo.presentation.mapper.toPresentationError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AppState(
    private val activatorManager: ScratchCardActivatorManager,
    scope: CoroutineScope,
) {

    private val _errorChannel = Channel<PresentationError?>(capacity = 16)
    val errorFlow = _errorChannel.receiveAsFlow()

    init {
        scope.launch {
            activatorManager.state.filterNotNull().collect {
                _errorChannel.send(it.asError?.throwable?.toPresentationError())
            }
        }
    }
}
