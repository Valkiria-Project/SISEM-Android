package com.skgtecnologia.sisem.data.authcards.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skgtecnologia.sisem.data.authcards.cache.model.ConfigEntity

@Dao
interface ConfigDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(configEntity: ConfigEntity)

    @Query("SELECT * FROM config LIMIT 1")
    suspend fun getConfig(): ConfigEntity?
}
