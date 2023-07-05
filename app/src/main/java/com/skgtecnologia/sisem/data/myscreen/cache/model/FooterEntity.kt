package com.skgtecnologia.sisem.data.myscreen.cache.model

import androidx.room.ColumnInfo
import com.skgtecnologia.sisem.domain.myscreen.model.FooterModel

data class FooterEntity(
    @ColumnInfo(name = "buttonList") val buttonList: List<ButtonEntity>
)

fun FooterEntity.mapToDomain(): FooterModel {
    return with(this) {
        FooterModel(
            buttonModelList = buttonList.map { it.mapToDomain() }
        )
    }
}

fun FooterModel.mapToCache(): FooterEntity {
    return with(this) {
        FooterEntity(
            buttonList = buttonModelList.map { it.mapToCache() }
        )
    }
}
