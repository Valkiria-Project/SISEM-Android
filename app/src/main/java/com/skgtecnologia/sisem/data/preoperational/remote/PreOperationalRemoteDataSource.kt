package com.skgtecnologia.sisem.data.preoperational.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.preoperational.remote.model.SavePreOperationalBody
import com.skgtecnologia.sisem.data.preoperational.remote.model.buildFindingFormDataBody
import com.skgtecnologia.sisem.data.preoperational.remote.model.buildFindingImagesBody
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.data.remote.model.screen.Params
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.model.banner.ErrorModelFactory
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.preoperational.model.Novelty
import javax.inject.Inject

class PreOperationalRemoteDataSource @Inject constructor(
    private val errorModelFactory: ErrorModelFactory,
    private val preOperationalApi: PreOperationalApi
) {

    suspend fun getPreOperationalScreen(
        role: OperationRole,
        androidId: String,
        vehicleCode: String?,
        idTurn: String
    ): Result<ScreenModel> =
        apiCall(errorModelFactory) {
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

    suspend fun sendPreOperational(
        role: OperationRole,
        idTurn: String,
        findings: Map<String, Boolean>,
        inventoryValues: Map<String, Int>,
        fieldsValues: Map<String, String>
    ): Result<Unit> = apiCall(errorModelFactory) {
        preOperationalApi.sendPreOperational(
            savePreOperationalBody = SavePreOperationalBody(
                type = role.name,
                idTurn = idTurn.toInt(),
                findingValues = findings,
                inventoryValues = inventoryValues,
                fieldsValues = fieldsValues
            )
        )
    }

    suspend fun sendFinding(
        role: OperationRole,
        idTurn: String,
        novelties: List<Novelty>
    ): Result<Unit> = apiCall(errorModelFactory) {
        val images = novelties.flatMap { it.images }

        preOperationalApi.sendFinding(
            partMap = buildFindingFormDataBody(role, idTurn, novelties),
            images = buildFindingImagesBody(images)
        )
    }
}
