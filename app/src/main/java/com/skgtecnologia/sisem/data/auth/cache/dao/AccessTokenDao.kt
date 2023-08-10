package com.skgtecnologia.sisem.data.auth.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skgtecnologia.sisem.data.auth.cache.model.AccessTokenEntity

@Dao
interface AccessTokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccessToken(accessTokenEntity: AccessTokenEntity)

    @Query("SELECT * FROM access_token LIMIT 1")
    suspend fun getAccessToken(): AccessTokenEntity?

    @Query("SELECT * FROM access_token")
    suspend fun getAllAccessTokens(): List<AccessTokenEntity>
}
