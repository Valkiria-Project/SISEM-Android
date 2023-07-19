package com.skgtecnologia.sisem.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.skgtecnologia.sisem.data.auth.cache.dao.AccessTokenDao
import com.skgtecnologia.sisem.data.auth.cache.model.AccessTokenEntity

@Database(
    entities = [
        AccessTokenEntity::class
    ],
    version = 1
)
abstract class SisemDatabase : RoomDatabase() {

    abstract fun accessTokenDao(): AccessTokenDao
}
