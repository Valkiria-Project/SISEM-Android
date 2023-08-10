package com.skgtecnologia.sisem.data.auth.cache.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel

@Entity(
    tableName = "access_token",
    indices = [Index(value = ["role"], unique = true)]
)
data class AccessTokenEntity(
    @PrimaryKey @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "token") val accessToken: String,
    @ColumnInfo(name = "refresh_token") val refreshToken: String,
    @ColumnInfo(name = "type") val tokenType: String,
    @ColumnInfo(name = "user_name") val username: String,
    @ColumnInfo(name = "role") val role: String,
    @ColumnInfo(name = "is_admin") val isAdmin: Boolean,
    @ColumnInfo(name = "name_user") val nameUser: String,
    @Embedded(prefix = "pre_operational_") val preoperational: PreOperationalEntity,
    @Embedded(prefix = "turn_") val turn: TurnEntity
)

fun AccessTokenEntity.mapToDomain(): AccessTokenModel {
    return with(this) {
        AccessTokenModel(
            userId = userId,
            accessToken = accessToken,
            refreshToken = refreshToken,
            tokenType = tokenType,
            username = username,
            role = role,
            isAdmin = isAdmin,
            nameUser = nameUser,
            preoperational = preoperational.mapToDomain(),
            turn = turn.mapToDomain()
        )
    }
}

fun AccessTokenModel.mapToCache(): AccessTokenEntity {
    return with(this) {
        AccessTokenEntity(
            userId = userId,
            accessToken = accessToken,
            refreshToken = refreshToken,
            tokenType = tokenType,
            username = username,
            role = role,
            isAdmin = isAdmin,
            nameUser = nameUser,
            preoperational = preoperational.mapToCache(),
            turn = turn.mapToCache()
        )
    }
}
