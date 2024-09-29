package com.absut.cashcalculator.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.absut.cashcalculator.data.model.CashRecord

@Database(entities = [CashRecord::class], version = 1)
abstract class RecordsDatabase() : RoomDatabase() {

    abstract fun getRecordsDao(): RecordsDao

    companion object {

        @Volatile
        private var INSTANCE: RecordsDatabase? = null

        fun getDatabase(context: Context): RecordsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecordsDatabase::class.java,
                    "records_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}