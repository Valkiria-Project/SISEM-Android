package com.skgtecnologia.sisem.data.cache.converters

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.time.LocalDateTime

class CacheConverters {

    private val listOfStringAdapter: JsonAdapter<List<String>> =
        Moshi.Builder()
            .build()
            .adapter(
                Types.newParameterizedType(List::class.java, String::class.java)
            )

    private val mapOfStringAdapter: JsonAdapter<Map<String, String>> =
        Moshi.Builder()
            .build()
            .adapter(
                Types.newParameterizedType(Map::class.java, String::class.java, String::class.java)
            )

    @TypeConverter
    fun stringToList(value: String?): List<String>? {
        return if (value != null) listOfStringAdapter.fromJson(value) else null
    }

    @TypeConverter
    fun listToString(list: List<String>?): String? {
        return listOfStringAdapter.toJson(list)
    }

    @TypeConverter
    fun stringToMap(value: String?): Map<String, String>? {
        return if (value != null) mapOfStringAdapter.fromJson(value) else null
    }

    @TypeConverter
    fun mapToString(list: Map<String, String>?): String? {
        return mapOfStringAdapter.toJson(list)
    }

    @TypeConverter
    fun localDateTimeToString(value: LocalDateTime): String {
        return value.toString()
    }

    @TypeConverter
    fun stringToLocalDateTime(value: String): LocalDateTime {
        return LocalDateTime.parse(value)
    }
}
