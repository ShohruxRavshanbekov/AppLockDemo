package uz.futuresoft.applockdemo

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
import uz.futuresoft.applockdemo.ui.theme.AppLockDemoTheme

@Composable
fun AccessibilityServiceIsNotEnableDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = "Settings")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
//        dismissButton = {
//            TextButton(onClick = onDismissRequest) {
//                Text(text = "Cancel")
//            }
//        },
        icon = {},
        title = {
            Text(
                text = "Accessibility Service is not enabled!",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        },
        text = {
            Text(
                text = "To use the application, the Accessibility Service should be turned on.",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        },
//        shape = ,
//        containerColor =,
//        titleContentColor =,
//        textContentColor =,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    )
}

@Preview
@Composable
private fun AccessibilityServiceIsNotEnableDialogPreview() {
    AppLockDemoTheme {
        AccessibilityServiceIsNotEnableDialog(onDismissRequest = {}, onConfirm = {})
    }
}