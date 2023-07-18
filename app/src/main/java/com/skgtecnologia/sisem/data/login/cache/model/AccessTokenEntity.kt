package com.skgtecnologia.sisem.data.login.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.skgtecnologia.sisem.domain.login.model.AccessTokenModel

@Entity(
    tableName = "access_tokens",
    indices = [Index(value = ["role"], unique = true)]
)
data class AccessTokenEntity(
    @PrimaryKey val id: Int = 1,
    @ColumnInfo(name = "token") val accessToken: String,
    @ColumnInfo(name = "refresh_token") val refreshToken: String,
    @ColumnInfo(name = "type") val tokenType: String,
    @ColumnInfo(name = "user_name") val username: String,
    @ColumnInfo(name = "role") val role: String
)

fun AccessTokenEntity.mapToDomain(): AccessTokenModel {
    return with(this) {
        AccessTokenModel(
            accessToken = accessToken,
            refreshToken = refreshToken,
            tokenType = tokenType,
            username = username,
            role = role
        )
    }
}

fun AccessTokenModel.mapToCache(): AccessTokenEntity {
    return with(this) {
        AccessTokenEntity(
            accessToken = accessToken,
            refreshToken = refreshToken,
            tokenType = tokenType,
            username = username,
            role = role
        )
    }
}
