package com.absut.cashcalculator

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.absut.cashcalculator.data.model.GitHubRelease
import com.absut.cashcalculator.data.util.UpdateChecker
import com.absut.cashcalculator.ui.components.UpdateInfoDialog
import com.absut.cashcalculator.ui.screen.HomeScreen
import com.absut.cashcalculator.ui.screen.SavedRecordScreen
import com.absut.cashcalculator.ui.theme.CashCalculatorTheme
import com.absut.cashcalculator.util.MainViewModelFactory
import com.absut.cashcalculator.util.toIndianCurrencyString

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            CashCalculatorTheme {
                val viewModel: MainViewModel =
                    viewModel(factory = MainViewModelFactory(applicationContext))
                val context = LocalContext.current
                var updateInfo by remember { mutableStateOf<GitHubRelease?>(null) }
                var shouldCheckForUpdate by remember { mutableStateOf(true) } //to check update on app start

                LaunchedEffect(shouldCheckForUpdate) {
                    if (shouldCheckForUpdate){
                        val update = UpdateChecker.checkForUpdate(context)
                        update?.let { updateInfo = it }
                        shouldCheckForUpdate = false
                    }
                }

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            viewModel = viewModel,
                            onViewSavedRecordClick = { navController.navigate("saved_record") },
                            onShareClick = { shareIntent(this@MainActivity, viewModel) },
                            onCheckUpdateClick = { shouldCheckForUpdate = true })
                    }
                    composable("saved_record") {
                        SavedRecordScreen(
                            viewModel = viewModel,
                            onBackClick = navController::popBackStack
                        )
                    }
                }

                updateInfo?.let { release ->
                    UpdateInfoDialog(
                        releaseInfo = release,
                        onDismissRequest = { updateInfo = null })
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

