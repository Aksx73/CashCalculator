package com.absut.cashcalculator.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.absut.cashcalculator.ui.theme.CashCalculatorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedRecordScreen(modifier: Modifier = Modifier) {

    Scaffold(modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Saved Records") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                navigationIcon = {
                    IconButton(onClick = { /*todo*/ }) {
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
             items(5) {
                 SavedRecordListItem()
             }
        }

    }
}

@Composable
fun SavedRecordListItem(modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = Shapes().large,
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Notes: 10", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "12/05.2024 12 AM",
                    Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = "23,500",
                Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "2000x24 | 500x40 | 200x200 | 100x95",
                Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "Message", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.size(0.dp))
            Text(text = "Lorem Ipsum", style = MaterialTheme.typography.bodyMedium, maxLines = 3)
        }


    }
}

@Preview
@Composable
private fun RecordListItemPreview() {
    CashCalculatorTheme {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer
        ) {
            SavedRecordListItem()
        }
    }
}


@Preview
@Composable
private fun SavedRecordScreenPreview() {
    CashCalculatorTheme {
        SavedRecordScreen()
    }
}
