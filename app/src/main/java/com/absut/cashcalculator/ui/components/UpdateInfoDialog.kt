package com.absut.cashcalculator.ui.components

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.absut.cashcalculator.R
import com.absut.cashcalculator.data.model.GitHubRelease
import com.absut.cashcalculator.BuildConfig

@Composable
fun UpdateInfoDialog(releaseInfo: GitHubRelease, onDismissRequest: () -> Unit) {
    val context = LocalContext.current
    val currentVersion = BuildConfig.VERSION_NAME

    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = Modifier.widthIn(max = 288.dp),
        icon = {
            Icon(
                Icons.Default.Info,
                contentDescription = "Update Info",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .height(37.dp)
                    .width(27.dp),
            )
        },
        title = {
            Text(
                text = "Update Available",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        },
        text = {
            Text(
                text = stringResource(
                    R.string.update_available_text,
                    releaseInfo.tagName,
                    currentVersion
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp),
                horizontalArrangement = Arrangement.Absolute.Right,
            ) {
                TextButton(onClick = onDismissRequest) { Text("Later") }
                TextButton(
                    onClick = {
                        val browserIntent = Intent(Intent.ACTION_VIEW, releaseInfo.htmlUrl.toUri())
                        context.startActivity(browserIntent)
                        onDismissRequest()
                    }
                ) {
                    Text("Go to GitHub")
                }
            }
        },
        dismissButton = {},
    )
}