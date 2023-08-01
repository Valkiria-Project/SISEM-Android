package com.skgtecnologia.sisem.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.skgtecnologia.sisem.data.auth.cache.dao.AccessTokenDao
import com.skgtecnologia.sisem.data.auth.cache.model.AccessTokenEntity
import com.skgtecnologia.sisem.data.cache.converters.CacheConverters

@Database(
    entities = [
        AccessTokenEntity::class
    ],
    version = 1
)
@TypeConverters(CacheConverters::class)
abstract class SisemDatabase : RoomDatabase() {

    abstract fun accessTokenDao(): AccessTokenDao
}
