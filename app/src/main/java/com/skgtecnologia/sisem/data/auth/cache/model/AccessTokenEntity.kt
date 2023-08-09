package com.skgtecnologia.sisem.data.auth.cache.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.skgtecnologia.sisem.domain.login.model.AccessTokenModel

@Entity(
    tableName = "access_token",
    indices = [Index(value = ["role"], unique = true)]
)
data class AccessTokenEntity(
    @ColumnInfo(name = "token") val accessToken: String,
    @ColumnInfo(name = "refresh_token") val refreshToken: String,
    @ColumnInfo(name = "type") val tokenType: String,
    @ColumnInfo(name = "user_name") val username: String,
    @PrimaryKey val role: String,
    @Embedded(prefix = "pre_operational_") val preoperational: PreOperationalEntity,
    @Embedded(prefix = "turn_") val turn: TurnEntity,
    @ColumnInfo(name = "is_admin") val isAdmin: Boolean,
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "name_user") val nameUser: String
)

fun AccessTokenEntity.mapToDomain(): AccessTokenModel {
    return with(this) {
        AccessTokenModel(
            accessToken = accessToken,
            refreshToken = refreshToken,
            tokenType = tokenType,
            username = username,
            role = role,
            preoperational = preoperational.mapToDomain(),
            turn = turn.mapToDomain(),
            isAdmin = isAdmin,
            userId = userId,
            nameUser = nameUser
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
            role = role,
            preoperational = preoperational.mapToCache(),
            turn = turn.mapToCache(),
            isAdmin = isAdmin,
            userId = userId,
            nameUser = nameUser
        )
    }
}
