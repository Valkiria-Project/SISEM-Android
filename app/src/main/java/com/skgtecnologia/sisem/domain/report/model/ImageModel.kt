package com.skgtecnologia.sisem.domain.report.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.io.File

@Serializable
@Parcelize
data class ImageModel(
    val fileName: String,
    @Serializable(with = FileSerializer::class)
    val file: File
) : Parcelable

object FileSerializer : KSerializer<File> {
    override val descriptor = PrimitiveSerialDescriptor("File", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: File) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): File {
        return File(decoder.decodeString())
    }
}