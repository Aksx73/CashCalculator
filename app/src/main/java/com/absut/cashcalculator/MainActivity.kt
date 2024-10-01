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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.absut.cashcalculator.ui.components.OutlinedTextFieldWithCustomContentPadding
import com.absut.cashcalculator.ui.components.ResetAlertDialog
import com.absut.cashcalculator.ui.screen.HomeScreen
import com.absut.cashcalculator.ui.screen.SavedRecordScreen
import com.absut.cashcalculator.ui.theme.CashCalculatorTheme
import com.absut.cashcalculator.util.MainViewModelFactory
import com.absut.cashcalculator.util.Utils
import com.absut.cashcalculator.util.toIndianCurrencyString

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            CashCalculatorTheme {
                val viewModel: MainViewModel =
                    viewModel(factory = MainViewModelFactory(applicationContext))

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            viewModel = viewModel,
                            onViewSavedRecordClick = { navController.navigate("saved_record") },
                            onShareClick = { shareIntent(this@MainActivity, viewModel) })
                    }
                    composable("saved_record") {
                        SavedRecordScreen(
                            viewModel = viewModel,
                            onBackClick = navController::popBackStack
                        )
                    }
                }

            }
        }
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

