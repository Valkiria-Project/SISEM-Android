package com.skgtecnologia.sisem.data.incident.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.skgtecnologia.sisem.domain.incident.model.PatientModel

@Entity(tableName = "patient")
data class PatientEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "full_name") val fullName: String,
    @ColumnInfo(name = "id_aph") val idAph: Int
)

fun PatientEntity.mapToDomain(): PatientModel = with(this) {
    PatientModel(
        id = id,
        fullName = fullName,
        idAph = idAph
    )
}

fun PatientModel.mapToCache(): PatientEntity = with(this) {
    PatientEntity(
        id = id,
        fullName = fullName,
        idAph = idAph
    )
}
