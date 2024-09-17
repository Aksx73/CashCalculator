package com.absut.cashcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.absut.cashcalculator.ui.theme.CashCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CashCalculatorTheme {
                CashCalculatorApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashCalculatorApp(modifier: Modifier = Modifier, viewModel: MainViewModel = viewModel()) {

    var showMenu by remember { mutableStateOf(false) }

    Scaffold(modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Cash Calculator",
                        //style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {/*todo*/ }) {
                        Icon(Icons.Outlined.Refresh, contentDescription = "Reset")
                    }
                },
                actions = {
                    IconButton(onClick = { /*todo*/ }) {
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
                                // Handle item click
                                showMenu = false
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
                                // Handle item click
                                showMenu = false
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
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                LazyColumn {
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

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Total: ₹${viewModel.total}",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Notes: ${viewModel.totalNotes}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun DenominationRow(denomination: Int, count: Int, onCountChange: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = getDenominationImageResource(denomination)),
            contentDescription = "₹$denomination note",
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "₹$denomination")
        Spacer(modifier = Modifier.weight(1f))
        TextField(
            value = count.toString(),
            onValueChange = { newValue ->
                val newCount = newValue.toIntOrNull() ?: 0
                onCountChange(newCount)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.width(80.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "= ₹${denomination * count}")
    }
}

fun getDenominationImageResource(denomination: Int): Int {
    // Replace with actual resource IDs for your denomination images
    return when (denomination) {
        2000 -> R.drawable.ic_android_black_24dp
        500 -> R.drawable.ic_android_black_24dp
        200 -> R.drawable.ic_android_black_24dp
        100 -> R.drawable.ic_android_black_24dp
        50 -> R.drawable.ic_android_black_24dp
        20 -> R.drawable.ic_android_black_24dp
        10 -> R.drawable.ic_android_black_24dp
        else -> R.drawable.ic_android_black_24dp
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CashCalculatorTheme {
        CashCalculatorApp()
    }
}