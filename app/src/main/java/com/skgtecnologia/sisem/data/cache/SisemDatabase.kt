package com.skgtecnologia.sisem.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.skgtecnologia.sisem.data.login.cache.dao.AccessTokenDao
import com.skgtecnologia.sisem.data.login.cache.model.AccessTokenEntity

@Database(
    entities = [
        AccessTokenEntity::class
    ],
    version = 1
)
abstract class SisemDatabase : RoomDatabase() {

    abstract fun accessTokenDao(): AccessTokenDao
}
