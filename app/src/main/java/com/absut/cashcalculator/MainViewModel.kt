package com.absut.cashcalculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.absut.cashcalculator.data.model.CashRecord
import com.absut.cashcalculator.data.repository.CashRecordRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: CashRecordRepository
) : ViewModel() {
    val denominations = listOf(2000, 500, 200, 100, 50, 20, 10)
    var counts = mutableStateListOf<String>()
    var total by mutableLongStateOf(0)
    var totalNotes by mutableIntStateOf(0)

    val savedRecord = repository.getSavedRecords()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    init {
        resetCalculator()
    }

    fun updateCount(index: Int, count: String) {
        counts[index] = count
        calculateTotal()
    }

    private fun calculateTotal() {
        total = denominations.zip(counts)
            .sumOf { (denom, count) -> denom * (count.toLongOrNull() ?: 0) }
        totalNotes = counts.sumOf { it.toIntOrNull() ?: 0 }
    }

    fun resetCalculator() {
        counts.clear()
        counts.addAll(List(denominations.size) { "" })
        total = 0
        totalNotes = 0
    }

    fun saveRecord(record: CashRecord) {
        viewModelScope.launch {
            repository.saveCashRecord(record)
        }
    }

    fun deleteRecord(record: CashRecord) {
        viewModelScope.launch {
            repository.deleteRecord(record)
        }
    }

}
