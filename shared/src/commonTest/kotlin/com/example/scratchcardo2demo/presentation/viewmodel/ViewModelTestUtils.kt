package com.example.scratchcardo2demo.presentation.viewmodel

import com.example.scratchcardo2demo.domain.`interface`.ScratchCardPersistenceRepository
import com.example.scratchcardo2demo.domain.model.ScratchCard
import dev.mokkery.MockMode.autoUnit
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import kotlinx.coroutines.flow.MutableStateFlow

internal fun mockPersistenceRepository(
    initialValue: ScratchCard? = null,
): Pair<ScratchCardPersistenceRepository, MutableStateFlow<ScratchCard?>> {
    val repository = mock<ScratchCardPersistenceRepository>(autoUnit)
    val state = MutableStateFlow(initialValue)
    every { repository.state } returns state
    return repository to state
}
