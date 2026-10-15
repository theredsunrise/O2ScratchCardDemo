package com.example.scratchcardo2demo.domain.manager

import com.example.scratchcardo2demo.domain.`interface`.ScratchCardActivatorManager
import com.example.scratchcardo2demo.domain.model.ScratchCard
import com.example.scratchcardo2demo.domain.usecase.ActivateCardUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class DefaultScratchCardActivatorManager(
    private val activateCardUseCase: ActivateCardUseCase,
    private val scope: CoroutineScope,
) : ScratchCardActivatorManager {

    override val state = Channel<Result<ScratchCard>>(Channel.BUFFERED)

    private var activationJob: Job? = null

    override fun activate() {
        cancel()
        activationJob = scope.launch {
            try {
                state.send(Result.success(activateCardUseCase()))
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (throwable: Throwable) {
                state.send(Result.failure(throwable))
            }
        }
    }

    override fun cancel() {
        activationJob?.cancel()
        activationJob = null
    }
}
