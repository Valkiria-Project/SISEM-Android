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

    @Query("SELECT * FROM access_token order by date_time desc LIMIT 1")
    suspend fun getAccessToken(): AccessTokenEntity?

    @Query("SELECT * FROM access_token")
    suspend fun getAllAccessTokens(): List<AccessTokenEntity>

    @Query(
        """
            UPDATE access_token
            SET pre_operational_status = false
            WHERE role = :role
             """
    )
    suspend fun updatePreOperationalStatus(role: String)

    @Query("DELETE FROM access_token")
    suspend fun deleteAccessToken() // FIXME: review this method

    @Query("DELETE FROM access_token WHERE user_name = :username")
    suspend fun deleteAccessTokenByUsername(username: String)
}
