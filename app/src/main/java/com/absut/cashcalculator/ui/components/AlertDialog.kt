package com.absut.cashcalculator.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ResetAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    positiveButtonTitle: String,
    negativeButtonTitle: String,
) {
    AlertDialog(
        /*icon = {
            Icon(icon, contentDescription = "Example Icon")
        },*/
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(positiveButtonTitle)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(negativeButtonTitle)
            }
        }
    )
}

@Preview
@Composable
private fun AlertDialogPreview() {
    ResetAlertDialog(
        onDismissRequest = { /*TODO*/ },
        onConfirmation = { /*TODO*/ },
        dialogTitle = "Reset cash count values",
        dialogText = "Reset cash count values",
        icon = Icons.Default.Call,
        positiveButtonTitle = "Reset",
        negativeButtonTitle = "Cancel"
    )

}