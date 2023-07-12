package com.skgtecnologia.sisem.data.myscreen.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.skgtecnologia.sisem.data.converters.ButtonCacheConverters
import com.skgtecnologia.sisem.data.converters.BodyRowCacheConverters
import com.skgtecnologia.sisem.data.myscreen.cache.model.MyScreenEntity

@Database(
    entities = [
        MyScreenEntity::class
    ],
    version = 1
)
@TypeConverters(
    ButtonCacheConverters::class,
    BodyRowCacheConverters::class
)
abstract class MyScreenDatabase : RoomDatabase() {

    abstract fun myScreenDao(): MyScreenDao
}
