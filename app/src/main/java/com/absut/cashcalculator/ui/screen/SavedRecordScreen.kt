package com.absut.cashcalculator.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.absut.cashcalculator.MainViewModel
import com.absut.cashcalculator.data.model.CashRecord
import com.absut.cashcalculator.ui.theme.CashCalculatorTheme
import com.absut.cashcalculator.util.toIndianCurrencyString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedRecordScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {

    val savedRecords by viewModel.savedRecord.collectAsStateWithLifecycle()

    Scaffold(modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Saved Records") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                },
            )
        }) { innerpadding ->

        LazyColumn(
            modifier = Modifier
				.fillMaxSize()
				.padding(innerpadding)
				.background(color = MaterialTheme.colorScheme.surfaceContainer),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(savedRecords) { record ->
                SavedRecordListItem(record = record)
            }
        }

    }
}

@Composable
fun SavedRecordListItem(modifier: Modifier = Modifier, record: CashRecord) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = Shapes().large,
        modifier = modifier.padding(vertical = 6.dp)
    ) {
        Column(
            modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Notes: ${record.totalNotes}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = record.formattedDate,
                    Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = record.total.toIndianCurrencyString(),
                Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = getNoteDescriptionString(record.noteDescription),
                Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "Message", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.size(0.dp))
            Text(
                text = record.message ?: "-",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3
            )
        }

    }
}

fun getNoteDescriptionString(noteDescription: Map<Int, String>): String {
    val result = StringBuilder()
    for ((denom, count) in noteDescription) {
        if (count.isNotEmpty() && (count.toLongOrNull() ?: 0) > 0) {
            result.append("₹${denom}x$count")

            if (denom != noteDescription.keys.last()) {
                result.append(" | ")
            }
        }
    }
    return result.toString()
}


@Preview
@Composable
private fun RecordListItemPreview() {
    CashCalculatorTheme {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer
        ) {
            SavedRecordListItem(
                record = CashRecord(
                    id = 1,
                    total = 234300,
                    noteDescription = mapOf(2000 to "1", 500 to "1", 100 to "1"),
                    totalNotes = 45,
                    message = null,
                    date = System.currentTimeMillis()
                )
            )
        }
    }
}


@Preview
@Composable
private fun SavedRecordScreenPreview() {
    CashCalculatorTheme {
        //SavedRecordScreen()
    }
}
