package com.example.scratchcardo2demo.domain.`interface`

import com.example.scratchcardo2demo.domain.model.ScratchCard
import kotlinx.coroutines.channels.Channel

interface ScratchCardActivatorManager {
    val state: Channel<Result<ScratchCard>>
    fun activate()
    fun cancel()
}
