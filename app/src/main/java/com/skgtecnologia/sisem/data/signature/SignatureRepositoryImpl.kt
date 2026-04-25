package com.skgtecnologia.sisem.data.signature

import com.skgtecnologia.sisem.data.signature.remote.SignatureRemoteDataSource
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.signature.SignatureRepository
import javax.inject.Inject

class SignatureRepositoryImpl @Inject constructor(
    private val signatureRemoteDataSource: SignatureRemoteDataSource
) : SignatureRepository {

    override suspend fun getInitSignatureScreen(): ScreenModel =
        signatureRemoteDataSource.getInitSignatureScreen().getOrThrow()

    override suspend fun getSignatureScreen(document: String): ScreenModel =
        signatureRemoteDataSource.getSignatureScreen(document).getOrThrow()

    override suspend fun searchDocument(document: String) =
        signatureRemoteDataSource.searchDocument(document).getOrThrow()

    override suspend fun registerSignature(document: String, signature: String) =
        signatureRemoteDataSource.registerSignature(document, signature).getOrThrow()
}
