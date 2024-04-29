package com.skgtecnologia.sisem.data.operation.cache.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.skgtecnologia.sisem.domain.authcards.model.OperationModel

@Entity(tableName = "operation")
data class OperationEntity(
    @PrimaryKey val id: Int = 1,
    @ColumnInfo(name = "preoperational_time") val preoperationalTime: Long,
    @ColumnInfo(name = "clinic_hist_observations_time") val clinicHistObservationsTime: Long,
    @ColumnInfo(name = "login_time") val loginTime: Long,
    @ColumnInfo(name = "num_img_preoperational_driver") val numImgPreoperationalDriver: Int,
    @ColumnInfo(name = "num_img_preoperational_doctor") val numImgPreoperationalDoctor: Int,
    @ColumnInfo(name = "num_img_preoperational_aux") val numImgPreoperationalAux: Int,
    @ColumnInfo(name = "num_img_novelty") val numImgNovelty: Int,
    @ColumnInfo(name = "auth_method") val authMethod: String,
    @ColumnInfo(name = "attentions_type") val attentionsType: String,
    @ColumnInfo(name = "status") val status: Boolean,
    @ColumnInfo(name = "vehicle_code") val vehicleCode: String?,
    @Embedded(prefix = "vehicle_config_") val vehicleConfig: VehicleConfigEntity?,
    @ColumnInfo(name = "max_file_sizeKb") val maxFileSizeKb: String,
    @ColumnInfo(name = "preoperational_exec") val preoperationalExec: Map<String, String>
)

fun OperationEntity.mapToDomain(): OperationModel {
    return with(this) {
        OperationModel(
            preoperationalTime = preoperationalTime,
            clinicHistObservationsTime = clinicHistObservationsTime,
            loginTime = loginTime,
            numImgPreoperationalDriver = numImgPreoperationalDriver,
            numImgPreoperationalDoctor = numImgPreoperationalDoctor,
            numImgPreoperationalAux = numImgPreoperationalAux,
            numImgNovelty = numImgNovelty,
            authMethod = authMethod,
            attentionsType = attentionsType,
            status = status,
            vehicleCode = vehicleCode,
            vehicleConfig = vehicleConfig?.mapToDomain(),
            maxFileSizeKb = maxFileSizeKb,
            preoperationalExec = preoperationalExec.mapToDomain(),
        )
    }
}

fun Map<String, String>.mapToDomain(): Map<String, Boolean> {
    return buildMap {
        this@mapToDomain.map {
            val boolean = it.value.toBoolean()
            put(it.key, boolean)
        }
    }
}

fun OperationModel.mapToCache(): OperationEntity {
    return with(this) {
        OperationEntity(
            preoperationalTime = preoperationalTime,
            clinicHistObservationsTime = clinicHistObservationsTime,
            loginTime = loginTime,
            numImgPreoperationalDriver = numImgPreoperationalDriver,
            numImgPreoperationalDoctor = numImgPreoperationalDoctor,
            numImgPreoperationalAux = numImgPreoperationalAux,
            numImgNovelty = numImgNovelty,
            authMethod = authMethod,
            attentionsType = attentionsType,
            status = status,
            vehicleCode = vehicleCode,
            vehicleConfig = vehicleConfig?.mapToCache(),
            maxFileSizeKb = maxFileSizeKb,
            preoperationalExec = preoperationalExec.mapToCache(),
        )
    }
}

fun Map<String, Boolean>.mapToCache(): Map<String, String> {
    return buildMap {
        this@mapToCache.map {
            val string = it.value.toString()
            put(it.key, string)
        }
    }
}
