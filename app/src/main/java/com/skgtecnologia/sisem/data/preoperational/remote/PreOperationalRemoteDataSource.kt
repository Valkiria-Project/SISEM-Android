package com.skgtecnologia.sisem.data.preoperational.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.data.remote.model.screen.Params
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class PreOperationalRemoteDataSource @Inject constructor(
    private val preOperationalApi: PreOperationalApi
) {

    suspend fun getPreOperationalScreen(operationRole: OperationRole): Result<ScreenModel> =
        apiCall {
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
            }
        }.mapResult {
            it.mapToDomain()
        }

    suspend fun sendPreOperational(): Result<Unit> = apiCall {
        preOperationalApi.sendPreOperational(
            screenBody = ScreenBody(params = Params(serial = "serial")) // FIXME: Hardcoded data
        )
    }
}
