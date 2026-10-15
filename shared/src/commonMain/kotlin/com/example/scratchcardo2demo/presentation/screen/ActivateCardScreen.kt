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
import com.example.scratchcardo2demo.presentation.model.ActivateCardUiIntent
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiPhaseId
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiState
import com.example.scratchcardo2demo.presentation.screen.component.ScratchCard

@Composable
fun ActivateCardScreen(
    modifier: Modifier,
    uiState: State<ScratchCardUiState>,
    onIntent: (ActivateCardUiIntent) -> Unit
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
            actionForPhase = ScratchCardUiPhaseId.ACTIVATED,
            onClick = {
                onIntent(ActivateCardUiIntent.Activate)
            },
            onError = {
                onIntent(ActivateCardUiIntent.Activate)
            }
        )
    }
}