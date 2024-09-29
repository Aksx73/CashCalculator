package com.absut.cashcalculator.data.model

import android.text.format.DateFormat
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cash_records")
data class CashRecord(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val total: Long,
    val noteDescription: Map<Int, String>, // {(2000,34),(500,20)}
    val totalNotes: Long,
    val message: String?,
    val date: Long = System.currentTimeMillis()
) {
    val formattedDate = DateFormat.format("dd/MM/yyyy hh:mm aa", date)
}
