package com.skgtecnologia.sisem.data.medicalhistory.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.medicalhistory.remote.model.DeleteAphFileBody
import com.skgtecnologia.sisem.data.medicalhistory.remote.model.MedicalHistoryBody
import com.skgtecnologia.sisem.data.medicalhistory.remote.model.mapToBody
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.data.remote.extensions.createRequestBody
import com.skgtecnologia.sisem.data.remote.model.screen.Params
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.report.model.ImageModel
import com.valkiria.uicomponents.components.humanbody.HumanBodyUi
import okhttp3.MultipartBody
import javax.inject.Inject

private const val SEND_APH_FILE_NAME = "files"

class MedicalHistoryRemoteDataSource @Inject constructor(
    private val medicalHistoryApi: MedicalHistoryApi
) {

    suspend fun getMedicalHistoryScreen(code: String, idAph: String): Result<ScreenModel> =
        apiCall {
            medicalHistoryApi.getMedicalHistoryScreen(
                screenBody = ScreenBody(
                    params = Params(
                        code = code,
                        idAph = idAph
                    )
                )
            )
        }.mapResult {
            it.mapToDomain()
        }

    suspend fun getVitalSignsScreen(): Result<ScreenModel> = apiCall {
        medicalHistoryApi.getVitalSignsScreen(
            screenBody = ScreenBody(
                params = Params()
            )
        )
    }.mapResult {
        it.mapToDomain()
    }

    suspend fun getMedicineScreen(serial: String): Result<ScreenModel> = apiCall {
        medicalHistoryApi.getMedicineScreen(
            screenBody = ScreenBody(
                params = Params(
                    serial = serial
                )
            )
        )
    }.mapResult {
        it.mapToDomain()
    }

    @Suppress("LongParameterList")
    suspend fun sendMedicalHistory(
        idTurn: String,
        idAph: String,
        humanBodyValues: List<HumanBodyUi>,
        segmentedValues: Map<String, Boolean>,
        signatureValues: Map<String, String>,
        fieldsValue: Map<String, String>,
        sliderValues: Map<String, String>,
        dropDownValues: Map<String, String>,
        chipSelectionValues: Map<String, String>,
        chipOptionsValues: Map<String, List<String>>,
        imageButtonSectionValues: Map<String, String>,
        vitalSigns: Map<String, Map<String, String>>,
        infoCardButtonValues: List<Map<String, String>>
    ): Result<Unit> = apiCall {
        medicalHistoryApi.sendMedicalHistory(
            medicalHistoryBody = MedicalHistoryBody(
                idTurn = idTurn,
                idAph = idAph,
                humanBodyValues = humanBodyValues.map { it.mapToBody() },
                segmentedValues = segmentedValues,
                signatureValues = signatureValues,
                fieldsValue = fieldsValue,
                sliderValues = sliderValues,
                dropDownValues = dropDownValues,
                chipSelectionValues = chipSelectionValues,
                chipOptionsValues = chipOptionsValues,
                imageButtonSectionValues = imageButtonSectionValues,
                vitalSigns = vitalSigns,
                infoCardButtonValues = infoCardButtonValues
            )
        )
    }

    suspend fun saveAphFiles(
        idAph: String,
        images: List<ImageModel>,
        description: String? = null
    ): Result<Unit> = apiCall {
        medicalHistoryApi.saveAphFiles(
            idAph = idAph.createRequestBody(),
            files = images.map { imageModel ->
                val requestFile = imageModel.file.createRequestBody()
                MultipartBody.Part.createFormData(
                    SEND_APH_FILE_NAME,
                    imageModel.file.name,
                    requestFile
                )
            },
            description = description?.createRequestBody()
        )
    }

    suspend fun getMedicalHistoryViewScreen(code: String, idAph: String): Result<ScreenModel> =
        apiCall {
            medicalHistoryApi.getMedicalHistoryViewScreen(
                screenBody = ScreenBody(
                    params = Params(
                        code = code,
                        idAph = idAph
                    )
                )
            )
        }.mapResult {
            it.mapToDomain()
        }

    suspend fun deleteAphFile(idAph: String, fileName: String): Result<Unit> = apiCall {
        medicalHistoryApi.deleteAphFile(DeleteAphFileBody(idAph = idAph, fileName = fileName))
    }
}
