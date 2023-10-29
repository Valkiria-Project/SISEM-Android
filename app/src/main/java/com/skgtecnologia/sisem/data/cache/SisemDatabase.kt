package com.skgtecnologia.sisem.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.skgtecnologia.sisem.data.auth.cache.dao.AccessTokenDao
import com.skgtecnologia.sisem.data.auth.cache.model.AccessTokenEntity
import com.skgtecnologia.sisem.data.cache.converters.CacheConverters
import com.skgtecnologia.sisem.data.operation.cache.dao.OperationDao
import com.skgtecnologia.sisem.data.operation.cache.model.OperationEntity

@Database(
    entities = [
        AccessTokenEntity::class,
        OperationEntity::class
    ],
    version = 5
)
@TypeConverters(CacheConverters::class)
abstract class SisemDatabase : RoomDatabase() {

    abstract fun accessTokenDao(): AccessTokenDao

    abstract fun configDao(): OperationDao
}
