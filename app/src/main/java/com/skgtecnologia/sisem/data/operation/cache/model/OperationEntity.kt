package com.skgtecnologia.sisem.data.operation.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.skgtecnologia.sisem.domain.authcards.model.ConfigModel

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
    @ColumnInfo(name = "ambulance_code") val ambulanceCode: String
)

fun OperationEntity.mapToDomain(): ConfigModel {
    return with(this) {
        ConfigModel(
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
            ambulanceCode = ambulanceCode
        )
    }
}

fun ConfigModel.mapToCache(): OperationEntity {
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
            ambulanceCode = ambulanceCode
        )
    }
}
