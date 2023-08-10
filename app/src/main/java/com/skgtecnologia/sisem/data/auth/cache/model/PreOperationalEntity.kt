package com.skgtecnologia.sisem.data.auth.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.skgtecnologia.sisem.domain.auth.model.PreOperationalModel

@Entity(tableName = "pre_operational",)
data class PreOperationalEntity(
    @PrimaryKey val id: Int = 1,
    @ColumnInfo(name = "preoperational") val preoperational: String,
    @ColumnInfo(name = "type_resource") val typeResource: String,
    @ColumnInfo(name = "status") val status: Boolean
)

fun PreOperationalEntity.mapToDomain(): PreOperationalModel {
    return with(this) {
        PreOperationalModel(
            preoperational = preoperational,
            typeResource = typeResource,
            status = status
        )
    }
}

fun PreOperationalModel.mapToCache(): PreOperationalEntity {
    return with(this) {
        PreOperationalEntity(
            preoperational = preoperational,
            typeResource = typeResource,
            status = status
        )
    }
}
