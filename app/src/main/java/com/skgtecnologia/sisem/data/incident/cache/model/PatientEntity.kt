package com.skgtecnologia.sisem.data.incident.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.valkiria.uicomponents.components.incident.model.PatientUiModel

@Entity(tableName = "patient")
data class PatientEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "full_name") val fullName: String,
    @ColumnInfo(name = "id_aph") val idAph: Int,
    @ColumnInfo(name = "is_pending_aph") val isPendingAph: Boolean
)

fun PatientEntity.mapToUi(): PatientUiModel = with(this) {
    PatientUiModel(
        id = id,
        fullName = fullName,
        idAph = idAph,
        isPendingAph = isPendingAph
    )
}

fun PatientUiModel.mapToCache(): PatientEntity = with(this) {
    PatientEntity(
        id = id,
        fullName = fullName,
        idAph = idAph,
        isPendingAph = isPendingAph
    )
}
