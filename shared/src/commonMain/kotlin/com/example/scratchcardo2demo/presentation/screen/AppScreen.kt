package com.example.scratchcardo2demo.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiState
import com.example.scratchcardo2demo.presentation.navigation.AppDestination
import com.example.scratchcardo2demo.presentation.screen.component.ScratchCard
import org.jetbrains.compose.resources.stringResource
import scratchcardo2demo.shared.generated.resources.Res
import scratchcardo2demo.shared.generated.resources.scratch_card_activate_action
import scratchcardo2demo.shared.generated.resources.scratch_card_scratch_action

@Composable
fun AppScreen(
    modifier: Modifier,
    uiState: State<ScratchCardUiState>,
    onNavigate: (AppDestination) -> Unit
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
            onClick = {
                onNavigate(AppDestination.ScratchCard)
            },
            onError = {}
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            onNavigate(AppDestination.ScratchCard)
        }) {
            Text(stringResource(Res.string.scratch_card_scratch_action))
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            onNavigate(AppDestination.ActivateCard)
        }) {
            Text(stringResource(Res.string.scratch_card_activate_action))
        }
    }
}
