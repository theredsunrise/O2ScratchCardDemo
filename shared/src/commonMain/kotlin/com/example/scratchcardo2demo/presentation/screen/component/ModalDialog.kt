package com.example.scratchcardo2demo.presentation.screen.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.example.scratchcardo2demo.presentation.mapper.PresentationError
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import scratchcardo2demo.shared.generated.resources.Res
import scratchcardo2demo.shared.generated.resources.error_title
import scratchcardo2demo.shared.generated.resources.ok_button

@Composable
fun ModalDialog(
    title: StringResource,
    messageState: State<PresentationError?>,
    onConfirm: () -> Unit
) {
        messageState.value?.let { message ->
            val messageText = when (message) {
                is PresentationError.Resource -> stringResource(message.value)
                is PresentationError.Text -> message.value
            }
            AlertDialog(
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
            onDismissRequest = onConfirm,
            title = { Text(stringResource(title)) },
                text = { Text(messageText) },
            confirmButton = {
                Button(onClick = onConfirm) {
                    Text(stringResource(Res.string.ok_button))
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ModalDialogPreview() {
    val errorState = remember {
        mutableStateOf<PresentationError?>(PresentationError.Text("Error message"))
    }
    ModalDialog(
        Res.string.error_title,
        errorState
    ) {
        errorState.value = null
    }
}
