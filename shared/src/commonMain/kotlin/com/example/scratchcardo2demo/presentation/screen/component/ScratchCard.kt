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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiPhase
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiPhaseId
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiLoadingAndError
import com.example.scratchcardo2demo.presentation.model.ScratchCardUiState
import com.example.scratchcardo2demo.presentation.mapper.PresentationError
import org.jetbrains.compose.resources.stringResource
import scratchcardo2demo.shared.generated.resources.Res
import scratchcardo2demo.shared.generated.resources.error_title
import scratchcardo2demo.shared.generated.resources.scratch_card_identifier
import scratchcardo2demo.shared.generated.resources.scratch_card_retry_action
import scratchcardo2demo.shared.generated.resources.scratch_card_status
import kotlin.uuid.Uuid

@Composable
fun ScratchCard(
    modifier: Modifier = Modifier,
    uiState: State<ScratchCardUiState>,
    actionForPhase: ScratchCardUiPhaseId? = null,
    onClick: () -> Unit,
    onError: () -> Unit
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

            val loadingAndError = stateValue.loadingAndError
            when (loadingAndError) {
                is ScratchCardUiLoadingAndError.Nothing, is ScratchCardUiLoadingAndError.Loading -> {
                    Text(
                        text = getUuid(state = stateValue),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = getStatus(state = stateValue),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }

                is ScratchCardUiLoadingAndError.Error -> {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = stringResource(Res.string.error_title),
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    Spacer(Modifier.height(16.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = loadingAndError.error.asText(),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            actionForPhase?.also { phase ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    if (loadingAndError.isLoading) {
                        CircularProgressIndicator()
                    } else if (
                        loadingAndError.isError ||
                        stateValue.isCurrentPhaseBelow(
                            phaseId = phase
                        )
                    ) {
                        Button(
                            onClick = {
                                if (loadingAndError.isError) {
                                    onError()
                                } else {
                                    onClick()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                if (loadingAndError.isError) {
                                    stringResource(Res.string.scratch_card_retry_action)
                                } else {
                                    phase.actionName()
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
private fun PresentationError.asText(): String = when (this) {
    is PresentationError.Resource -> stringResource(value)
    is PresentationError.Text -> value
}

@Composable
private fun getUuid(state: ScratchCardUiState?): String {
    return state?.uuid?.let { uuid ->
        stringResource(Res.string.scratch_card_identifier) + uuid
    }.orEmpty()
}

@Composable
private fun getStatus(state: ScratchCardUiState?): String {
    return state?.phase?.id?.statusName()?.let { status ->
        stringResource(Res.string.scratch_card_status) + status
    }.orEmpty()
}

@Composable
@Preview
private fun ScratchCardWithButtonPreview() {
    ScratchCard(
        modifier = Modifier.fillMaxSize(),
        uiState = mutableStateOf(
            ScratchCardUiState(
                loadingAndError = ScratchCardUiLoadingAndError.Nothing,
                phase = ScratchCardUiPhase.Scratched(cardUuid = Uuid.random())
            )
        ),
        actionForPhase = ScratchCardUiPhaseId.ACTIVATED,
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
            ScratchCardUiState(
                loadingAndError = ScratchCardUiLoadingAndError.Nothing,
                phase = ScratchCardUiPhase.Scratched(cardUuid = Uuid.random())
            )
        ),
        actionForPhase = ScratchCardUiPhaseId.UNSCRATCHED,
        onClick = {},
        onError = {}
    )
}

@Composable
@Preview
private fun ScratchCardErrorStatePreview() {
    ScratchCard(
        modifier = Modifier.fillMaxSize(),
        uiState = mutableStateOf(
            ScratchCardUiState(
                loadingAndError = ScratchCardUiLoadingAndError.Error(PresentationError.Text("Error message")),
                phase = ScratchCardUiPhase.Scratched(cardUuid = Uuid.random())
            )
        ),
        actionForPhase = ScratchCardUiPhaseId.ACTIVATED,
        onClick = {},
        onError = {}
    )
}

@Composable
@Preview
private fun ScratchCardLoadingStatePreview() {
    ScratchCard(
        modifier = Modifier.fillMaxSize(),
        uiState = mutableStateOf(
            ScratchCardUiState(
                loadingAndError = ScratchCardUiLoadingAndError.Loading,
                phase = ScratchCardUiPhase.Scratched(cardUuid = Uuid.random())
            )
        ),
        actionForPhase = ScratchCardUiPhaseId.ACTIVATED,
        onClick = {},
        onError = {}
    )
}
