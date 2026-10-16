package com.example.scratchcardo2demo.domain.manager

import com.example.scratchcardo2demo.domain.`interface`.ScratchCardActivatorManager
import com.example.scratchcardo2demo.domain.model.ScratchCard
import com.example.scratchcardo2demo.domain.usecase.ActivateCardUseCase
import com.example.scratchcardo2demo.shared.ResultExt
import com.example.scratchcardo2demo.shared.assertIsNotMainThread
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class DefaultScratchCardActivatorManager(
    private val activateCardUseCase: ActivateCardUseCase,
    private val scope: CoroutineScope,
) : ScratchCardActivatorManager {

    private val _state =
        MutableSharedFlow<ResultExt<ScratchCard>?>(
            replay = 1,
            extraBufferCapacity = 16,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    override val state = _state

    private var activationJob: Job? = null

    init {
        scope.launch {
            _state.emit(value = null)
        }
    }

    override fun activate() {
        cancel()
        activationJob = scope.launch {
            try {
                //assertIsNotMainThread()
                state.emit(ResultExt.Loading)
                state.emit(ResultExt.Success(data = activateCardUseCase()))
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (throwable: Throwable) {
                state.emit(ResultExt.Error(throwable))
            }
        }
    }

    override fun cancel() {
        activationJob?.cancel()
        activationJob = null
    }
}
