package com.skgtecnologia.sisem.data.preoperational.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.preoperational.remote.model.SavePreOperationalBody
import com.skgtecnologia.sisem.data.preoperational.remote.model.mapToBody
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.data.remote.model.screen.Params
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.model.error.ErrorModelFactory
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.preoperational.model.Novelty
import javax.inject.Inject

class PreOperationalRemoteDataSource @Inject constructor(
    private val errorModelFactory: ErrorModelFactory,
    private val preOperationalApi: PreOperationalApi
) {

    suspend fun getPreOperationalScreen(operationRole: OperationRole): Result<ScreenModel> =
        apiCall(errorModelFactory) {
            when (operationRole) {
                OperationRole.AUXILIARY_AND_OR_TAPH -> preOperationalApi.getAuxPreOperationalScreen(
                    screenBody = ScreenBody(
                        params = Params(
                            serial = "12312JHJKG22",
                            code = "0404",
                            turnId = "1"
                        )
                    ) // FIXME: Hardcoded data
                )

                OperationRole.DRIVER -> preOperationalApi.getDriverPreOperationalScreen(
                    screenBody = ScreenBody(
                        params = Params(
                            serial = "12312JHJKG22",
                            code = "0404",
                            turnId = "1"
                        )
                    ) // FIXME: Hardcoded data
                )

                OperationRole.MEDIC_APH -> preOperationalApi.getDoctorPreOperationalScreen(
                    screenBody = ScreenBody(
                        params = Params(
                            serial = "12312JHJKG22",
                            code = "0404",
                            turnId = "1"
                        )
                    ) // FIXME: Hardcoded data
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
    ): Result<Unit> = apiCall(errorModelFactory) {
        preOperationalApi.sendPreOperational(
            savePreOperationalBody = SavePreOperationalBody(
                type = role.name,
                idTurn = idTurn.toInt(),
                findingValues = findings,
                inventoryValues = inventoryValues,
                fieldsValues = fieldsValues,
                novelties = novelties.map { it.mapToBody() }
            )
        )
    }
}
