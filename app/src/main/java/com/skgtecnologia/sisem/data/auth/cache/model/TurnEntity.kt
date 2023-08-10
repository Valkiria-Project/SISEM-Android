package com.skgtecnologia.sisem.data.auth.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.skgtecnologia.sisem.domain.auth.model.TurnModel

@Entity(tableName = "turns")
data class TurnEntity(
    @PrimaryKey val id: Int = 1,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "isComplete") val isComplete: Boolean
)

fun TurnEntity.mapToDomain(): TurnModel {
    return with(this) {
        TurnModel(
            id = id,
            date = date,
            isComplete = isComplete
        )
    }
}

fun TurnModel.mapToCache(): TurnEntity {
    return with(this) {
        TurnEntity(
            id = id,
            date = date,
            isComplete = isComplete
        )
    }
}
