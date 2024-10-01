package com.absut.cashcalculator.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.absut.cashcalculator.MainViewModel
import com.absut.cashcalculator.data.model.CashRecord
import com.absut.cashcalculator.data.repository.CashRecordRepository

class MainViewModelFactory(private val context: Context): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CashRecord::class.java)){
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(CashRecordRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}