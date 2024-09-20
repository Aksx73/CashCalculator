package com.absut.cashcalculator.util

import java.text.NumberFormat
import java.util.Locale

fun Long.toIndianCurrencyString(): String {
    val number =this.toDouble()
    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    formatter.maximumFractionDigits = 0
    return formatter.format(number)
}
