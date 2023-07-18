package com.skgtecnologia.sisem.data.login.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skgtecnologia.sisem.data.login.cache.model.AccessTokenEntity

@Dao
interface AccessTokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccessToken(accessTokenEntity: AccessTokenEntity)

    @Query("SELECT * FROM access_tokens LIMIT 1") // FIXME: Adjust this query by Role
    suspend fun getAccessToken(): AccessTokenEntity?
}
