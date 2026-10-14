package com.example.scratchcardo2demo.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scratchcardo2demo.presentation.model.ScratchCardUIIntent
import com.example.scratchcardo2demo.presentation.model.ScratchCardUIPhaseId
import com.example.scratchcardo2demo.presentation.model.ScratchCardUIState
import com.example.scratchcardo2demo.presentation.screen.component.ScratchCard
import scratchcardo2demo.shared.generated.resources.Res
import scratchcardo2demo.shared.generated.resources.scratch_card_scratch_action

@Composable
fun ScratchCardScreen(
    modifier: Modifier,
    uiState: State<ScratchCardUIState>,
    onIntent: (ScratchCardUIIntent) -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = CenterHorizontally
    ) {
        ScratchCard(
            modifier = Modifier.fillMaxSize(),
            uiState = uiState,
            phaseId = ScratchCardUIPhaseId.SCRATCHED,
            onClick = {
                onIntent(ScratchCardUIIntent.Scratch)
            },
            onError = {
                onIntent(ScratchCardUIIntent.Scratch)
            }
        )
    }
}