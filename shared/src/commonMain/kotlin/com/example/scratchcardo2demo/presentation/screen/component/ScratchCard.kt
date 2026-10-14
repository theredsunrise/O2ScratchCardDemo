package com.example.scratchcardo2demo.presentation.screen.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scratchcardo2demo.domain.model.ScratchCard
import com.example.scratchcardo2demo.presentation.model.ScratchCardUIPhase
import com.example.scratchcardo2demo.presentation.model.ScratchCardUIPhaseId
import com.example.scratchcardo2demo.presentation.model.ScratchCardUIState
import org.jetbrains.compose.resources.stringResource
import scratchcardo2demo.shared.generated.resources.Res
import scratchcardo2demo.shared.generated.resources.scratch_card_identifier
import scratchcardo2demo.shared.generated.resources.scratch_card_retry_action
import scratchcardo2demo.shared.generated.resources.scratch_card_status
import kotlin.uuid.Uuid

@Composable
fun ScratchCard(
    modifier: Modifier = Modifier,
    uiState: State<ScratchCardUIState>,
    phaseId: ScratchCardUIPhaseId? = null,
    onClick: (() -> Unit)? = null,
    onError: (() -> Unit)? = null
) {
    val stateValue = uiState.value
    Card(
        modifier = modifier
            .fillMaxWidth().requiredHeight(300.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Spacer(Modifier.height(16.dp))

            Text(
                text = getUuid(state = stateValue.asSuccess),
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(Modifier.height(16.dp))

            Text(
                text = getStatus(state = stateValue.asSuccess),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.weight(1f))

            phaseId?.also { phase ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    if (stateValue.isLoading) {
                        CircularProgressIndicator()
                    } else if (
                        stateValue.isError ||
                        stateValue.isCurrentPhaseBelow(
                            phaseId = phase
                        )
                    ) {
                        Button(
                            onClick = {
                                if (stateValue.isSuccess) {
                                    onClick?.invoke()
                                } else {
                                    onError?.invoke()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                if (stateValue.isSuccess) {
                                    phase.name()
                                } else {
                                    stringResource(Res.string.scratch_card_retry_action)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun getUuid(state: ScratchCardUIState.Success?): String {
    return state?.uuid?.let { uuid ->
        stringResource(Res.string.scratch_card_identifier) + uuid
    }.orEmpty()
}

@Composable
private fun getStatus(state: ScratchCardUIState.Success?): String {
    return state?.phase?.id?.name()?.let { status ->
        stringResource(Res.string.scratch_card_status) + status
    }.orEmpty()
}

@Composable
@Preview
private fun ScratchCardWithButtonPreview() {
    ScratchCard(
        modifier = Modifier.fillMaxSize(),
        uiState = mutableStateOf(
            ScratchCardUIState.Success(
                phase = ScratchCardUIPhase.Scratched(cardUuid = Uuid.random())
            )
        ),
        phaseId = ScratchCardUIPhaseId.ACTIVATED,
        onClick = {},
        onError = {}
    )
}

@Composable
@Preview
private fun ScratchCardWithoutButtonPreview() {
    ScratchCard(
        modifier = Modifier.fillMaxSize(),
        uiState = mutableStateOf(
            ScratchCardUIState.Success(
                phase = ScratchCardUIPhase.Scratched(cardUuid = Uuid.random())
            )
        )
    )
}

@Composable
@Preview
private fun ScratchCardErrorStatePreview() {
    ScratchCard(
        modifier = Modifier.fillMaxSize(),
        uiState = mutableStateOf(
            ScratchCardUIState.Error(Exception("Error"))
        ),
        phaseId = ScratchCardUIPhaseId.ACTIVATED,
        onError = {}

    )
}