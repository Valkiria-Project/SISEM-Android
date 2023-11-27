package com.skgtecnologia.sisem.data.cache.converters

import androidx.room.TypeConverter
import com.skgtecnologia.sisem.data.incident.cache.model.PatientEntity
import com.skgtecnologia.sisem.data.incident.cache.model.ResourceEntity
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.time.LocalDateTime

@Suppress("TooManyFunctions")
class CacheConverters {

    private val listOfStringAdapter: JsonAdapter<List<String>> = Moshi.Builder()
        .build()
        .adapter(
            Types.newParameterizedType(List::class.java, String::class.java)
        )

    @TypeConverter
    fun stringToList(value: String?): List<String>? {
        return if (value != null) listOfStringAdapter.fromJson(value) else null
    }

    @TypeConverter
    fun listToString(list: List<String>?): String? {
        return listOfStringAdapter.toJson(list)
    }

    private val listOfPatientAdapter: JsonAdapter<List<PatientEntity>> = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
        .adapter(
            Types.newParameterizedType(List::class.java, PatientEntity::class.java)
        )

    @TypeConverter
    fun stringToPatientList(value: String?): List<PatientEntity>? {
        return if (value != null) listOfPatientAdapter.fromJson(value) else null
    }

    @TypeConverter
    fun listPatientToString(list: List<PatientEntity>?): String? {
        return listOfPatientAdapter.toJson(list)
    }

    private val listOfResourceAdapter: JsonAdapter<List<ResourceEntity>> = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
        .adapter(
            Types.newParameterizedType(List::class.java, ResourceEntity::class.java)
        )

    @TypeConverter
    fun stringToResourceList(value: String?): List<ResourceEntity>? {
        return if (value != null) listOfResourceAdapter.fromJson(value) else null
    }

    @TypeConverter
    fun listResourceToString(list: List<ResourceEntity>?): String? {
        return listOfResourceAdapter.toJson(list)
    }

    private val mapOfStringAdapter: JsonAdapter<Map<String, String>> = Moshi.Builder()
        .build()
        .adapter(
            Types.newParameterizedType(Map::class.java, String::class.java, String::class.java)
        )

    @TypeConverter
    fun stringToMap(value: String?): Map<String, String>? {
        return if (value != null) mapOfStringAdapter.fromJson(value) else null
    }

    @TypeConverter
    fun mapToString(map: Map<String, String>?): String? {
        return mapOfStringAdapter.toJson(map)
    }

    private val listOfMapOfStringAdapter: JsonAdapter<List<Map<String, String>>> = Moshi.Builder()
        .build()
        .adapter(
            Types.newParameterizedType(
                List::class.java,
                Map::class.java,
                String::class.java,
                String::class.java
            )
        )

    @TypeConverter
    fun stringToListOfMap(value: String?): List<Map<String, String>>? {
        return if (value != null) listOfMapOfStringAdapter.fromJson(value) else null
    }

    @TypeConverter
    fun listOfMapToString(list: List<Map<String, String>>?): String? {
        return listOfMapOfStringAdapter.toJson(list)
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
