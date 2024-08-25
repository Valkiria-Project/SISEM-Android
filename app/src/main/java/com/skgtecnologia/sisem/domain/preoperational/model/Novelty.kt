package com.skgtecnologia.sisem.domain.preoperational.model

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.skgtecnologia.sisem.domain.report.model.ImageModel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
@Parcelize
data class Novelty(
    val idPreoperational: String,
    val novelty: String,
    val images: List<ImageModel>
) : Parcelable

val NoveltyType = object : NavType<Novelty>(
    isNullableAllowed = true
) {
    override fun get(bundle: Bundle, key: String): Novelty? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, Novelty::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): Novelty {
        return Json.decodeFromString<Novelty>(value)
    }

    override fun serializeAsValue(value: Novelty): String {
        return Json.encodeToString(value)
    }

    override fun put(bundle: Bundle, key: String, value: Novelty) {
        bundle.putParcelable(key, value)
    }
}
