package com.example.scratchcardo2demo.domain.`interface`

import com.example.scratchcardo2demo.domain.model.ScratchCard
import com.example.scratchcardo2demo.shared.ResultExt
import kotlinx.coroutines.flow.Flow

interface ScratchCardActivatorManager {
    val state: Flow<ResultExt<ScratchCard>?>
    fun activate()
    fun cancel()
}
