package com.absut.cashcalculator.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.absut.cashcalculator.MainViewModel
import com.absut.cashcalculator.data.model.CashRecord
import com.absut.cashcalculator.ui.components.ResetAlertDialog
import com.absut.cashcalculator.ui.theme.CashCalculatorTheme
import com.absut.cashcalculator.util.toIndianCurrencyString
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedRecordScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val savedRecords by viewModel.savedRecords.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showSnackbar by remember { mutableStateOf(false) }
    var deletedRecord: CashRecord? by remember { mutableStateOf(null) }
    var restoredRecordId: Int? by remember { mutableStateOf(null) }

    Scaffold(modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
            contentPadding = PaddingValues(/*horizontal = 16.dp,*/ vertical = 8.dp)
        ) {
            items(savedRecords, key = {it.id }) { record ->
                val isRestored = record.id == restoredRecordId

                SwipeBox(
                    onFromRightSwipe = {
                        //delete record and save it in variable for undo purpose
                        if(!isRestored) {
                            deletedRecord = record
                            viewModel.deleteRecord(record)
                            showSnackbar = true
                        }
                    },
                    onFromLeftSwipe = {
                        //delete record and save it in variable for undo purpose
                        if(!isRestored) {
                            deletedRecord = record
                            viewModel.deleteRecord(record)
                            showSnackbar = true
                        }
                    },
                    modifier = Modifier.animateItem(
                        fadeInSpec = null,
                        fadeOutSpec = null
                    )
                ) {
                    SavedRecordListItem(record = record)
                }
            }
        }

        if (showSnackbar){
            LaunchedEffect(Unit) {
                val snackbarResult = snackbarHostState.showSnackbar(
                    "Record deleted!",
                    duration = SnackbarDuration.Long,
                    actionLabel = "Undo"
                )
                if (snackbarResult == SnackbarResult.ActionPerformed) {
                    deletedRecord?.let {
                        viewModel.saveRecord(it)
                        restoredRecordId = it.id
                    }
                    deletedRecord = null // Reset deletedRecord
                }
                showSnackbar = false // Reset showSnackbar
            }
        }

    }
}

@Composable
fun SavedRecordListItem(modifier: Modifier = Modifier, record: CashRecord) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = Shapes().large,
        modifier = modifier.padding(vertical = 6.dp, horizontal = 16.dp)
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
                text = if (record.message.isNullOrBlank()) "-" else record.message,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3
            )
        }

    }
}

@Composable
private fun SwipeBox(
    modifier: Modifier = Modifier,
    onFromRightSwipe: () -> Unit,
    onFromLeftSwipe: () -> Unit,
   content: @Composable () -> Unit
) {
    val swipeState = rememberSwipeToDismissBoxState(positionalThreshold = { it * .5f })

    lateinit var icon: ImageVector
    lateinit var alignment: Alignment
    val color: Color

    when (swipeState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> {
            icon = Icons.Outlined.Delete
            alignment = Alignment.CenterEnd
            color = MaterialTheme.colorScheme.errorContainer
        }

        SwipeToDismissBoxValue.StartToEnd -> {
            icon = Icons.Outlined.Delete
            alignment = Alignment.CenterStart
            color = MaterialTheme.colorScheme.errorContainer
        }

        SwipeToDismissBoxValue.Settled -> {
            icon = Icons.Outlined.Delete
            alignment = Alignment.CenterEnd
            color = Color.Transparent
        }
    }

    SwipeToDismissBox(
        modifier = modifier.animateContentSize(),
        state = swipeState,
        backgroundContent = {
            Box(
                contentAlignment = alignment,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 6.dp, horizontal = 16.dp)
                    .background(color, shape = Shapes().large),
            ) {
                Icon(
                    modifier = Modifier.minimumInteractiveComponentSize(),
                    imageVector = icon, contentDescription = null
                )
            }
        }
    ) {
        content()
    }

    when (swipeState.currentValue) {
        SwipeToDismissBoxValue.EndToStart -> {
            LaunchedEffect(swipeState) {
                onFromRightSwipe()
                swipeState.reset()
            }
        }

        SwipeToDismissBoxValue.StartToEnd -> {
            LaunchedEffect(swipeState) {
                onFromLeftSwipe()
                swipeState.snapTo(SwipeToDismissBoxValue.Settled)
            }
        }

        else -> {}
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
