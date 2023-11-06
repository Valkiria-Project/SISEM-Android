package com.skgtecnologia.sisem.data.auth.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skgtecnologia.sisem.data.auth.cache.model.AccessTokenEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccessTokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccessToken(accessTokenEntity: AccessTokenEntity)

    @Query("SELECT * FROM access_token order by date_time desc LIMIT 1")
    fun observeAccessToken(): Flow<AccessTokenEntity?>

    @Query("SELECT * FROM access_token")
    suspend fun getAllAccessTokens(): List<AccessTokenEntity>

    @Query(
        """
            UPDATE access_token
            SET pre_operational_status = :status
            WHERE role = :role
             """
    )
    suspend fun updatePreOperationalStatus(role: String, status: Boolean)

    @Query("DELETE FROM access_token")
    suspend fun deleteAccessToken()

    @Query("DELETE FROM access_token WHERE user_name = :username")
    suspend fun deleteAccessTokenByUsername(username: String)
}
