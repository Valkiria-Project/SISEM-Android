package com.skgtecnologia.sisem.data.cache.converters

import androidx.room.TypeConverter
import com.skgtecnologia.sisem.domain.login.model.PreoperationalModel
import com.skgtecnologia.sisem.domain.login.model.TurnModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class CacheConverters {

    private val listOfStringAdapter: JsonAdapter<List<String>> =
        Moshi.Builder()
            .build()
            .adapter(
                Types.newParameterizedType(List::class.java, String::class.java)
            )

    private val preoperationalAdapter: JsonAdapter<PreoperationalModel> =
        Moshi.Builder()
            .build()
            .adapter(
                Types.newParameterizedType(PreoperationalModel::class.java, String::class.java)
            )

    private val turnAdapter: JsonAdapter<TurnModel> =
        Moshi.Builder()
            .build()
            .adapter(
                Types.newParameterizedType(TurnModel::class.java, String::class.java)
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
    fun stringToPreoperational(value: String?): PreoperationalModel? {
        return if (value != null) preoperationalAdapter.fromJson(value) else null
    }

    @TypeConverter
    fun preoperationalToString(preoperational: PreoperationalModel?): String? {
        return preoperationalAdapter.toJson(preoperational)
    }

    @TypeConverter
    fun stringToTurn(value: String?): TurnModel? {
        return if (value != null) turnAdapter.fromJson(value) else null
    }

    @TypeConverter
    fun turnToString(turn: TurnModel?): String? {
        return turnAdapter.toJson(turn)
    }
}
