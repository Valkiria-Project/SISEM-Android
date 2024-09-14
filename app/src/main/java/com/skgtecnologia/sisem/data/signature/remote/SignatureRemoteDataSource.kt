package com.skgtecnologia.sisem.data.signature.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.core.data.remote.api.NetworkApi
import com.skgtecnologia.sisem.core.data.remote.model.screen.Params
import com.skgtecnologia.sisem.core.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.core.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.data.signature.remote.model.SignatureBody
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class SignatureRemoteDataSource @Inject constructor(
    private val networkApi: NetworkApi,
    private val signatureApi: SignatureApi
) {

    suspend fun getInitSignatureScreen(): Result<ScreenModel> = networkApi.apiCall {
        signatureApi.getInitSignatureScreen(
            screenBody = ScreenBody(params = Params())
        )
    }.mapResult {
        it.mapToDomain()
    }

    suspend fun getSignatureScreen(document: String): Result<ScreenModel> = networkApi.apiCall {
        signatureApi.getSignatureScreen(
            screenBody = ScreenBody(
                params = Params(
                    document = document
                )
            )
        )
    }.mapResult {
        it.mapToDomain()
    }

    suspend fun searchDocument(document: String): Result<Unit> = networkApi.apiCall {
        signatureApi.searchDocument(document = document)
    }

    suspend fun registerSignature(document: String, signature: String): Result<Unit> =
        networkApi.apiCall {
            signatureApi.registerSignature(
                signatureBody = SignatureBody(
                    document = document,
                    signature = signature
                )
            )
        }
}
