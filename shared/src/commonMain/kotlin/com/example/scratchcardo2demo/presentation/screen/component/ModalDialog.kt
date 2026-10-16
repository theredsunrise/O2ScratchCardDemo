package com.example.scratchcardo2demo.presentation.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import scratchcardo2demo.shared.generated.resources.Res
import scratchcardo2demo.shared.generated.resources.error_title
import scratchcardo2demo.shared.generated.resources.ok_button

@Composable
fun ModalDialog(
    title: StringResource,
    messageState: State<String?>,
    onConfirm: () -> Unit,
) {
    messageState.value?.let { message ->
        Dialog(
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
            ),
            onDismissRequest = onConfirm,
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                shape = MaterialTheme.shapes.extraLarge,
                tonalElevation = 6.dp,
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = stringResource(title),
                        style = MaterialTheme.typography.headlineSmall,
                    )

                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    Spacer(Modifier.height(24.dp))
                    Button(
                        modifier = Modifier.align(Alignment.End),
                        onClick = onConfirm,
                    ) {
                        Text(stringResource(Res.string.ok_button))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ModalDialogPreview() {
    val errorState = remember {
        mutableStateOf<String?>("Error message")
    }
    ModalDialog(
        title = Res.string.error_title,
        messageState = errorState,
    ) {
        errorState.value = null
    }
}
