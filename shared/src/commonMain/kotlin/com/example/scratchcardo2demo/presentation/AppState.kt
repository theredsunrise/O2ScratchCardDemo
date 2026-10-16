package com.example.scratchcardo2demo.presentation

import com.example.scratchcardo2demo.domain.`interface`.ScratchCardActivatorManager
import com.example.scratchcardo2demo.presentation.mapper.PresentationError
import com.example.scratchcardo2demo.presentation.mapper.toPresentationError
import com.example.scratchcardo2demo.shared.ResultExt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AppState(
    private val activatorManager: ScratchCardActivatorManager,
    scope: CoroutineScope,
) {

    private val _errorChannel = Channel<PresentationError>(capacity = 16)
    val errorFlow = _errorChannel.receiveAsFlow()

    init {
        scope.launch {
            activatorManager.state
                .filterNotNull()
                .filterIsInstance<ResultExt.Error>()
                .collect { errorResult ->
                    _errorChannel.send(errorResult.throwable.toPresentationError())
            }
        }
    }
}
