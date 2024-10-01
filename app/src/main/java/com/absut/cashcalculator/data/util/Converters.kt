package com.absut.cashcalculator.data.util

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MapConverter {
	@TypeConverter
	fun mapToJson(value: Map<Int, String>) = Json.encodeToString(value)

	@TypeConverter
	fun jsonToMap(value: String): Map<Int, String> = Json.decodeFromString(value)
}