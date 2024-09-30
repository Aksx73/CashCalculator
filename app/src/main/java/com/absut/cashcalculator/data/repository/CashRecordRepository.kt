package com.absut.cashcalculator.data.repository

import android.content.Context
import com.absut.cashcalculator.data.local.RecordsDatabase
import com.absut.cashcalculator.data.model.CashRecord

class CashRecordRepository(private val context: Context) {

    private val recordsDao = RecordsDatabase.getDatabase(context).getRecordsDao()

    suspend fun saveCashRecord(cashRecord: CashRecord) {
        recordsDao.saveCashRecord(cashRecord)
    }

    fun getSavedRecords() = recordsDao.getSavedRecords()

    suspend fun deleteRecord(cashRecord: CashRecord) {
        recordsDao.deleteRecord(cashRecord)
    }

}