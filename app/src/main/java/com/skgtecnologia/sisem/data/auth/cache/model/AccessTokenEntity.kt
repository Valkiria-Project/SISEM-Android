package com.skgtecnologia.sisem.data.auth.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.skgtecnologia.sisem.domain.login.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.login.model.PreoperationalModel
import com.skgtecnologia.sisem.domain.login.model.TurnModel

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
    @ColumnInfo(name = "role") val role: String,
    @ColumnInfo(name = "preoperational") val preoperational: PreoperationalModel,
    @ColumnInfo(name = "turn") val turn: TurnModel,
    @ColumnInfo(name = "is_admin") val isAdmin: Boolean
)

fun AccessTokenEntity.mapToDomain(): AccessTokenModel {
    return with(this) {
        AccessTokenModel(
            accessToken = accessToken,
            refreshToken = refreshToken,
            tokenType = tokenType,
            username = username,
            role = role,
            preoperational = preoperational,
            turn = turn,
            isAdmin = isAdmin
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
            preoperational = preoperational,
            turn = turn,
            isAdmin = isAdmin
        )
    }
}
