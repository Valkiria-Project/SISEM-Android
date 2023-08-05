package com.skgtecnologia.sisem.data.authcards.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.skgtecnologia.sisem.domain.authcards.model.ConfigModel

@Entity(tableName = "config")
data class ConfigEntity(
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
    @ColumnInfo(name = "status") val status: Boolean
)

fun ConfigEntity.mapToDomain(): ConfigModel {
    return with(this) {
        ConfigModel(
            id = id,
            preoperationalTime = preoperationalTime,
            clinicHistObservationsTime = clinicHistObservationsTime,
            loginTime = loginTime,
            numImgPreoperationalDriver = numImgPreoperationalDriver,
            numImgPreoperationalDoctor = numImgPreoperationalDoctor,
            numImgPreoperationalAux = numImgPreoperationalAux,
            numImgNovelty = numImgNovelty,
            authMethod = authMethod,
            attentionsType = attentionsType,
            status = status
        )
    }
}

fun ConfigModel.mapToCache(): ConfigEntity {
    return with(this) {
        ConfigEntity(
            id = id,
            preoperationalTime = preoperationalTime,
            clinicHistObservationsTime = clinicHistObservationsTime,
            loginTime = loginTime,
            numImgPreoperationalDriver = numImgPreoperationalDriver,
            numImgPreoperationalDoctor = numImgPreoperationalDoctor,
            numImgPreoperationalAux = numImgPreoperationalAux,
            numImgNovelty = numImgNovelty,
            authMethod = authMethod,
            attentionsType = attentionsType,
            status = status
        )
    }
}
