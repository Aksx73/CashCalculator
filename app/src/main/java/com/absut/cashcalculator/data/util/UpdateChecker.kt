package com.absut.cashcalculator.data.util

import android.content.Context
import android.util.Log
import com.absut.cashcalculator.BuildConfig
import com.absut.cashcalculator.R
import com.absut.cashcalculator.data.model.GitHubRelease
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

object UpdateChecker {
    private val client = OkHttpClient()
    private val jsonParser = Json { ignoreUnknownKeys = true }

    suspend fun checkForUpdate(context: Context): GitHubRelease? {
        return withContext(Dispatchers.IO) {
            val apiUrl = context.getString(R.string.update_url)
            try {
                val request = Request.Builder().url(apiUrl).build()
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        Log.e("UpdateChecker", "Failed to fetch releases: ${response.code}")
                        return@withContext null
                    }
                    val responseBody = response.body.string()
                    val latestRelease = jsonParser.decodeFromString<GitHubRelease>(responseBody)

                    val currentVersion = BuildConfig.VERSION_NAME
                    val latestVersion = latestRelease.tagName.removePrefix("v")
                    Log.d(
                        "UpdateChecker",
                        "Current version: $currentVersion, Latest GitHub release: $latestVersion",
                    )
                    if (latestVersion > currentVersion) {
                        Log.d("UpdateChecker", "New update found: ${latestRelease.tagName}")
                        return@withContext latestRelease
                    } else {
                        Log.d("UpdateChecker", "App is up to date.")
                        return@withContext null
                    }
                }
            } catch (e: Exception) {
                Log.e("UpdateChecker", "Error checking for update", e)
                return@withContext null
            }
        }
    }
}