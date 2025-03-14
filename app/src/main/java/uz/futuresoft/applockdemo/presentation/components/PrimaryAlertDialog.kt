package uz.futuresoft.applockdemo.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import uz.futuresoft.applockdemo.presentation.ui.theme.AppLockDemoTheme

@Composable
fun PrimaryAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    title: String? = null,
    text: String? = null,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = "Settings")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Cancel")
            }
        },
        icon = {},
        title = {
            Text(
                text = title ?: "",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        },
        text = {
            Text(
                text = text ?: "",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    )
}

@Preview
@Composable
private fun AccessibilityServiceIsNotEnableDialogPreview() {
    AppLockDemoTheme {
        PrimaryAlertDialog(
            onDismissRequest = {},
            onConfirm = {},
            title = "Accessibility Service is not enabled!",
            text = "To use the application, the Accessibility Service should be turned on.",
        )
    }
}