package com.skgtecnologia.sisem.data.converters

import androidx.room.TypeConverter
import com.skgtecnologia.sisem.data.myscreen.cache.model.ButtonEntity
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.ParameterizedType

class ButtonCacheConverters {

    private val parametrizedType: ParameterizedType =
        Types.newParameterizedType(List::class.java, ButtonEntity::class.java)
    private val jsonAdapter: JsonAdapter<List<ButtonEntity>> =
        Moshi.Builder().build().adapter(parametrizedType)

    @TypeConverter
    fun toButtonList(value: String?): List<ButtonEntity>? {
        return if (value != null) jsonAdapter.fromJson(value) else null
    }

    @TypeConverter
    fun fromButtonList(buttonList: List<ButtonEntity>?): String? {
        return jsonAdapter.toJson(buttonList)
    }
}
