package com.skgtecnologia.sisem.data.operation.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.skgtecnologia.sisem.domain.authcards.model.VehicleConfigModel

@Entity(tableName = "vehicle_config")
data class VehicleConfigEntity(
    @PrimaryKey val id: Int = 1,
    @ColumnInfo(name = "zone") val zone: String,
    @ColumnInfo(name = "status_code") val statusCode: String,
    @ColumnInfo(name = "provider") val provider: String,
    @ColumnInfo(name = "plate") val plate: String,
    @ColumnInfo(name = "preoperational") val preoperational: String,
    @ColumnInfo(name = "type_resource") val typeResource: String,
    @ColumnInfo(name = "status_color") val statusColor: String
)

fun VehicleConfigEntity.mapToDomain(): VehicleConfigModel {
    return with(this) {
        VehicleConfigModel(
            zone = zone,
            statusCode = statusCode,
            provider = provider,
            plate = plate,
            preoperational = preoperational,
            typeResource = typeResource,
            statusColor = statusColor
        )
    }
}

fun VehicleConfigModel.mapToCache(): VehicleConfigEntity {
    return with(this) {
        VehicleConfigEntity(
            zone = zone,
            statusCode = statusCode,
            provider = provider,
            plate = plate,
            preoperational = preoperational,
            typeResource = typeResource,
            statusColor = statusColor
        )
    }
}
