package com.absut.cashcalculator.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.absut.cashcalculator.data.model.CashRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCashRecord(cashRecord: CashRecord)

    @Query("SELECT * FROM cash_records")
    fun getSavedRecords(): Flow<List<CashRecord>>

    @Delete
    suspend fun deleteRecord(cashRecord: CashRecord)

}