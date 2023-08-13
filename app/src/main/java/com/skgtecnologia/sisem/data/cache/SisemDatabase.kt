package com.skgtecnologia.sisem.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.skgtecnologia.sisem.data.auth.cache.dao.AccessTokenDao
import com.skgtecnologia.sisem.data.auth.cache.model.AccessTokenEntity
import com.skgtecnologia.sisem.data.operation.cache.dao.OperationDao
import com.skgtecnologia.sisem.data.operation.cache.model.OperationEntity
import com.skgtecnologia.sisem.data.cache.converters.CacheConverters

@Database(
    entities = [
        AccessTokenEntity::class,
        OperationEntity::class
    ],
    version = 1
)
@TypeConverters(CacheConverters::class)
abstract class SisemDatabase : RoomDatabase() {

    abstract fun accessTokenDao(): AccessTokenDao

    abstract fun configDao(): OperationDao
}
