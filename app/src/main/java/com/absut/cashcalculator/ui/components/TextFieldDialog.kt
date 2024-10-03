package com.absut.cashcalculator.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TextFieldDialog(
    title: String = "Add note/message",
    confirmText: String = "Save",
    negativeText: String = "Cancel",
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                //Text(message)
                //Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Write your message here") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(text) }) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

}

@Preview
@Composable
private fun TextFieldDialogPreview() {
    TextFieldDialog(
        onDismiss = { /*TODO*/ },
        title = "Add note/message",
        confirmText = "Save"
    ) {
        // dismiss
    }
}