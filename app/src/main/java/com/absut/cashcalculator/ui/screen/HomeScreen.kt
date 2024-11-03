package com.absut.cashcalculator.ui.screen

import android.content.Context
import android.content.Intent
import android.content.Intent.createChooser
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Space
import android.widget.Toast
import androidx.activity.result.launch
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.lifecycle.viewmodel.compose.viewModel
import com.absut.cashcalculator.MainViewModel
import com.absut.cashcalculator.R
import com.absut.cashcalculator.data.model.CashRecord
import com.absut.cashcalculator.shareIntent
import com.absut.cashcalculator.ui.components.OutlinedTextFieldWithCustomContentPadding
import com.absut.cashcalculator.ui.components.ResetAlertDialog
import com.absut.cashcalculator.ui.components.TextFieldDialog
import com.absut.cashcalculator.ui.theme.CashCalculatorTheme
import com.absut.cashcalculator.util.Utils
import com.absut.cashcalculator.util.toIndianCurrencyString
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onViewSavedRecordClick: () -> Unit,
    onShareClick: () -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var showMenu by remember { mutableStateOf(false) }
    var openAlertDialog by remember { mutableStateOf(false) }
    var openAddNoteDialog by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    var showShareOptionBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val focusManager = LocalFocusManager.current
    val graphicsLayer = rememberGraphicsLayer()

    fun shareBitmapFromComposable() {
        coroutineScope.launch {
            val bitmap = graphicsLayer.toImageBitmap()
            val uri = bitmap.asAndroidBitmap().saveImage(context)
            shareBitmap(context, uri)
        }
    }

    Scaffold(modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Cash Calculator",
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                navigationIcon = {
                    IconButton(onClick = { openAlertDialog = true }) {
                        Icon(Icons.Outlined.Refresh, contentDescription = "Reset")
                    }
                },
                actions = {
                    IconButton(onClick = { showShareOptionBottomSheet = true }) {
                        Icon(Icons.Outlined.Share, contentDescription = "Share")
                    }
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        shape = Shapes().large
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                showMenu = false
                                if (viewModel.total > 0) {
                                    openAddNoteDialog = true
                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Cannot save empty record"
                                        )
                                    }
                                }

                            },
                            text = { Text(text = "Save record") },
                            leadingIcon = {
                                Icon(
                                    painterResource(id = R.drawable.ic_save_24),
                                    contentDescription = "Save Record"
                                )
                            }
                        )
                        DropdownMenuItem(
                            onClick = {
                                showMenu = false
                                onViewSavedRecordClick()
                            },
                            text = { Text(text = "View saved records") },
                            leadingIcon = {
                                Icon(
                                    painterResource(id = R.drawable.ic_database_24dp),
                                    contentDescription = "View history"
                                )
                            }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (openAlertDialog) {
            ResetAlertDialog(
                onDismissRequest = { openAlertDialog = false },
                onConfirmation = {
                    focusManager.clearFocus()
                    openAlertDialog = false
                    viewModel.resetCalculator()
                },
                dialogTitle = "Reset cash counts?",
                dialogText = "This action will remove all count records and cannot be undone",
                icon = Icons.Default.Info,
                "Reset", "Cancel"
            )
        }
        if (openAddNoteDialog) {
            TextFieldDialog(
                onDismiss = { openAddNoteDialog = false },
                onConfirm = { note ->
                    openAddNoteDialog = false

                    val record = createSaveRecordData(
                        viewModel,
                        note
                    )
                    viewModel.saveRecord(record)
                    Toast.makeText(
                        context,
                        "Record saved successfully",
                        Toast.LENGTH_LONG
                    ).show()
                    viewModel.resetCalculator()
                }
            )
        }
        if (showShareOptionBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showShareOptionBottomSheet = false
                },
                sheetState = sheetState,
                dragHandle = { BottomSheetDefaults.DragHandle() },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable {
                            onShareClick()
                            coroutineScope
                                .launch { sheetState.hide() }
                                .invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showShareOptionBottomSheet = false
                                    }
                                }
                        }
                ) {
                    Icon(
                        modifier = Modifier.padding(end = 16.dp),
                        painter = painterResource(R.drawable.ic_text_fields_24),
                        contentDescription = "Share text"
                    )
                    Text(text = "Share as Text")
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable {
                        focusManager.clearFocus()
                        shareBitmapFromComposable()
                        coroutineScope
                            .launch {
                                sheetState.hide()
                            }
                            .invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showShareOptionBottomSheet = false
                                }
                            }
                    }) {
                    Icon(
                        modifier = Modifier.padding(end = 16.dp),
                        painter = painterResource(R.drawable.ic_image_24),
                        contentDescription = "Share screenshot"
                    )
                    Text(text = "Share as Image")
                }
                Spacer(Modifier.size(8.dp))
            }
        }

        if (isLandscape) {
            LandscapeLayout(
                Modifier
                    .padding(innerPadding)
                    .windowInsetsPadding(WindowInsets.displayCutout)
                    .drawWithCache {
                        onDrawWithContent {
                            graphicsLayer.record {
                                this@onDrawWithContent.drawContent()
                            }
                            drawLayer(graphicsLayer)
                        }
                    },
                viewModel
            )
        }
        else {
            DefaultLayout(
                Modifier
                    .padding(innerPadding)
                    .drawWithCache {
                        onDrawWithContent {
                            graphicsLayer.record {
                                this@onDrawWithContent.drawContent()
                            }
                            drawLayer(graphicsLayer)
                        }
                    },
                viewModel
            )

        }
    }
}

@Composable
fun DefaultLayout(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = Shapes().large
                ),
        ) {
            Row(Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Notes: ${viewModel.totalNotes}",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = viewModel.total.toIndianCurrencyString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.size(24.dp))

        LazyColumn(
            modifier = Modifier
                .imePadding()
                .padding(horizontal = 16.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = Shapes().large
                ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {

            items(
                viewModel.denominations.zip(viewModel.counts).withIndex().toList()
            ) { (index, pair) ->
                val (denomination, count) = pair
                DenominationRow(
                    denomination = denomination,
                    count = count,
                    onCountChange = { newCount ->
                        viewModel.updateCount(index, newCount)
                    }
                )
            }
        }
    }
}

@Composable
fun LandscapeLayout(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(
            modifier = Modifier
                .weight(0.5f)
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = Shapes().large
                )
            //.align(Alignment.CenterVertically),
        ) {
            Row(Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Notes: ${viewModel.totalNotes}",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = viewModel.total.toIndianCurrencyString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                //.imePadding()
                .padding(top = 16.dp, bottom = 16.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = Shapes().large
                ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {

            items(
                viewModel.denominations.zip(viewModel.counts).withIndex().toList()
            ) { (index, pair) ->
                val (denomination, count) = pair
                DenominationRow(
                    denomination = denomination,
                    count = count,
                    onCountChange = { newCount ->
                        viewModel.updateCount(index, newCount)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.size(16.dp))
    }
}

@Composable
fun DenominationRow(denomination: Int, count: String, onCountChange: (String) -> Unit) {
    var isFocused by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = Utils.getDenominationImageResource(denomination)),
            contentDescription = "₹$denomination note",
            modifier = Modifier
                .width(52.dp)
                .height(21.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = "$denomination", textAlign = TextAlign.Start, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "x")
        Spacer(modifier = Modifier.width(24.dp))
        OutlinedTextFieldWithCustomContentPadding(
            value = count,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.toIntOrNull() != null) {
                    onCountChange(newValue)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .weight(1f)
                //.defaultMinSize(minWidth = 80.dp)
                //.height(54.dp)
                .onFocusChanged { isFocused = it.isFocused },
            shape = Shapes().medium,
            placeholder = {
                if (!isFocused) {
                    Text(
                        "0",
                        Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }
            },
            textStyle = TextStyle(textAlign = TextAlign.Center),
            singleLine = true,
            contentPadding = PaddingValues(10.dp)
        )
        Spacer(modifier = Modifier.width(24.dp))
        Text(text = "=")
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "${denomination * (count.toLongOrNull() ?: 0)}",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
}

fun createSaveRecordData(viewModel: MainViewModel, message: String?): CashRecord {
    val valueMap = viewModel.denominations.associateWith {
        viewModel.counts[viewModel.denominations.indexOf(it)]
    }
    val nonEmptyMap = valueMap.filterValues { it.isNotEmpty() }

    return CashRecord(
        totalNotes = viewModel.totalNotes,
        total = viewModel.total,
        noteDescription = nonEmptyMap,
        message = message,
        date = System.currentTimeMillis()
    )
}

private suspend fun Bitmap.saveImage(context: Context): Uri? {
    return try {
        val filename = "screenshot-${System.currentTimeMillis()}.png"
        val imageFile = File(context.cacheDir, filename)

        FileOutputStream(imageFile).use { outputStream ->
            this.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
        }

        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", imageFile)

    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun shareBitmap(context: Context, uri: Uri?) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    startActivity(context, createChooser(intent, "Share your image"), null)
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun GreetingPreview() {
    CashCalculatorTheme {
        HomeScreen(
            viewModel = viewModel<MainViewModel>(),
            onViewSavedRecordClick = {},
            onShareClick = {}
        )
    }
}