package com.absut.cashcalculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val denominations = listOf(2000, 500, 200, 100, 50, 20, 10)
    var counts = mutableStateListOf<String>()
    var total by mutableIntStateOf(0)
    var totalNotes by mutableIntStateOf(0)

    init {
        //counts.addAll(List(denominations.size) { 0 })
        resetCalculator()
    }

    fun updateCount(index: Int, count: String) {
        counts[index] = count
        calculateTotal()
    }

    private fun calculateTotal() {
        total = denominations.zip(counts).sumOf { (denom, count) -> denom * (count.toIntOrNull() ?: 0) }
        totalNotes = counts.sumOf { it.toIntOrNull() ?: 0 }
    }

    fun resetCalculator() {
        counts.clear()
        counts.addAll(List(denominations.size) { "" })
        total = 0
        totalNotes = 0
    }
}
