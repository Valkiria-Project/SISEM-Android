package com.skgtecnologia.sisem.data.auth.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skgtecnologia.sisem.data.auth.cache.model.AccessTokenEntity

@Dao
interface AccessTokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAccessToken(accessTokenEntity: AccessTokenEntity)

    @Query("SELECT * FROM access_token order by date_time desc LIMIT 1")
    fun getAccessToken(): AccessTokenEntity?

    @Query("SELECT * FROM access_token")
    fun getAllAccessTokens(): List<AccessTokenEntity>

    @Query("DELETE FROM access_token")
    fun deleteAccessToken() // FIXME: review this method
}
