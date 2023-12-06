package com.skgtecnologia.sisem.data.preoperational.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.data.preoperational.remote.model.SavePreOperationalBody
import com.skgtecnologia.sisem.data.preoperational.remote.model.mapToBody
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.data.remote.extensions.createRequestBody
import com.skgtecnologia.sisem.data.remote.model.screen.Params
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.preoperational.model.Novelty
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import okhttp3.MultipartBody
import javax.inject.Inject

private const val FINDING_FILE_NAME = "images"

class PreOperationalRemoteDataSource @Inject constructor(
    private val preOperationalApi: PreOperationalApi
) {

    suspend fun getPreOperationalScreen(
        role: OperationRole,
        androidId: String,
        vehicleCode: String?,
        idTurn: String
    ): Result<ScreenModel> = apiCall {
        val screenBody = ScreenBody(
            params = Params(
                serial = androidId,
                code = vehicleCode,
                turnId = idTurn
            )
        )

        when (role) {
            OperationRole.AUXILIARY_AND_OR_TAPH -> preOperationalApi.getAuxPreOperationalScreen(
                screenBody = screenBody
            )

            OperationRole.DRIVER -> preOperationalApi.getDriverPreOperationalScreen(
                screenBody = screenBody
            )

            OperationRole.MEDIC_APH -> preOperationalApi.getDoctorPreOperationalScreen(
                screenBody = screenBody
            )

            OperationRole.LEAD_APH ->
                throw IllegalArgumentException("Lead APH role not supported")
        }
    }.mapResult {
        it.mapToDomain()
    }

    @Suppress("LongParameterList")
    suspend fun sendPreOperational(
        role: OperationRole,
        idTurn: String,
        findings: Map<String, Boolean>,
        inventoryValues: Map<String, Int>,
        fieldsValues: Map<String, String>,
        novelties: List<Novelty>
    ): Result<Unit> = apiCall {
        preOperationalApi.sendPreOperational(
            savePreOperationalBody = SavePreOperationalBody(
                type = role.name,
                idTurn = idTurn.toInt(),
                findingValues = findings,
                inventoryValues = inventoryValues,
                fieldsValues = fieldsValues,
                novelties = novelties
                    .filter { it.images.isEmpty() }
                    .map { it.mapToBody() }
            )
        )
    }

    suspend fun sendFindings(
        role: OperationRole,
        idTurn: String,
        novelties: List<Novelty>
    ): Result<Unit> = resultOf {
        coroutineScope {
            novelties.map { novelty ->
                async {
                    validateAndSendFinding(novelty, role, idTurn)
                }
            }.awaitAll()
        }
    }

    private suspend fun validateAndSendFinding(
        novelty: Novelty,
        role: OperationRole,
        idTurn: String
    ) {
        if (novelty.images.isNotEmpty()) {
            apiCall {
                preOperationalApi.sendFinding(
                    type = role.name.createRequestBody(),
                    idPreoperational = novelty.idPreoperational.createRequestBody(),
                    idTurn = idTurn.createRequestBody(),
                    novelty = novelty.novelty.createRequestBody(),
                    images = novelty.images.map { imageModel ->
                        val requestFile = imageModel.file.createRequestBody()
                        MultipartBody.Part.createFormData(
                            FINDING_FILE_NAME,
                            imageModel.file.name,
                            requestFile
                        )
                    }
                )
            }
        }
    }

    suspend fun getAuthCardViewScreen(
        androidId: String,
        vehicleCode: String?,
        idTurn: String
    ): Result<ScreenModel> = apiCall {
        val screenBody = ScreenBody(
            params = Params(
                serial = androidId,
                code = vehicleCode,
                turnId = idTurn
            )
        )

        preOperationalApi.getAuthCardViewScreen(screenBody = screenBody)
    }.mapResult {
        it.mapToDomain()
    }

    suspend fun getPreOperationalScreenView(
        role: OperationRole,
        androidId: String,
        vehicleCode: String?,
        idTurn: String
    ): Result<ScreenModel> = apiCall {
        val screenBody = ScreenBody(
            params = Params(
                serial = androidId,
                code = vehicleCode,
                turnId = idTurn
            )
        )

        when (role) {
            OperationRole.AUXILIARY_AND_OR_TAPH -> preOperationalApi.getAuxPreOperationalScreenView(
                screenBody = screenBody
            )

            OperationRole.DRIVER -> preOperationalApi.getDriverPreOperationalScreenView(
                screenBody = screenBody
            )

            OperationRole.MEDIC_APH -> preOperationalApi.getDoctorPreOperationalScreenView(
                screenBody = screenBody
            )

            OperationRole.LEAD_APH ->
                throw IllegalArgumentException("Lead APH role not supported")
        }
    }.mapResult {
        it.mapToDomain()
    }
}
