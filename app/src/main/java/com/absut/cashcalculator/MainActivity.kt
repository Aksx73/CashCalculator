package com.absut.cashcalculator

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.absut.cashcalculator.ui.components.OutlinedTextFieldWithCustomContentPadding
import com.absut.cashcalculator.ui.components.ResetAlertDialog
import com.absut.cashcalculator.ui.theme.CashCalculatorTheme
import com.absut.cashcalculator.util.Utils
import com.absut.cashcalculator.util.toIndianCurrencyString

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            CashCalculatorTheme {
                val viewModel: MainViewModel = viewModel()

                CashCalculatorApp(
                    viewModel = viewModel,
                    onShareClick = { shareIntent(this, viewModel) }
                )

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashCalculatorApp(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onShareClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var openAlertDialog by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(modifier = modifier.fillMaxSize(),
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
                    IconButton(onClick = { onShareClick() }) {
                        Icon(Icons.Outlined.Share, contentDescription = "Share")
                    }
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                showMenu = false
                                //todo save result in history if not empty
                                // option to add description/note to the record before saving
                            },
                            text = { Text(text = "Save to history") },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Add,
                                    contentDescription = "View history"
                                )
                            }
                        )
                        DropdownMenuItem(
                            onClick = {
                                showMenu = false
                                //todo view history screen
                            },
                            text = { Text(text = "View history") },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Add,
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
                    openAlertDialog = false
                    viewModel.resetCalculator()
                },
                dialogTitle = "Reset cash counts?",
                dialogText = "This action will remove all count records and cannot be undone",
                icon = Icons.Default.Info
            )
        }

        if (isLandscape) {
            LandscapeLayout(Modifier.padding(innerPadding), viewModel)
        } else {
            DefaultLayout(Modifier.padding(innerPadding), viewModel)
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

fun getShareResult(viewModel: MainViewModel): String {
    val result = StringBuilder()
    val valueMap = viewModel.denominations.associateWith {
        viewModel.counts[viewModel.denominations.indexOf(it)]
    }

    result.appendLine("Cash Calculator Result")
    result.appendLine()
    for ((denomination, count) in valueMap) {
        if (count.isNotEmpty()) {
            val value: Long = denomination * (count.toLongOrNull() ?: 0)
            result.appendLine("$denomination x $count = $value")
        }
    }

    result.appendLine("------------------")
    result.appendLine("Total: ${viewModel.total.toIndianCurrencyString()}")

    return result.toString()
}

fun shareIntent(context: Activity, viewModel: MainViewModel) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, getShareResult(viewModel))
        type = "text/plain"
    }
    val shareIntent =
        Intent.createChooser(sendIntent, "Share cash calculation result via")
    context.startActivity(shareIntent)
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
@Preview(device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun GreetingPreview() {
    CashCalculatorTheme {
        CashCalculatorApp(
            viewModel = viewModel<MainViewModel>(),
            onShareClick = {}
        )
    }
}

