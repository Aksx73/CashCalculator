package com.absut.cashcalculator.util

import com.absut.cashcalculator.R
import java.text.NumberFormat
import java.util.Locale

fun Long.toIndianCurrencyString(): String {
    val number =this.toDouble()
    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    formatter.maximumFractionDigits = 0
    return formatter.format(number)
}

object Utils {

    fun getDenominationImageResource(denomination: Int): Int {
        return when (denomination) {
            2000 -> R.drawable.two_thousand_note
            500 -> R.drawable.five_hundred_note
            200 -> R.drawable.two_hundred_note
            100 -> R.drawable.hundred_note
            50 -> R.drawable.fifty_note
            20 -> R.drawable.twenty_note
            10 -> R.drawable.ten_note
            else -> R.drawable.ten_note
        }
    }



}