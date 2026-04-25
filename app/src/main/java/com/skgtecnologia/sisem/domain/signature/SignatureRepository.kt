package com.skgtecnologia.sisem.domain.signature

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel

interface SignatureRepository {

    suspend fun getInitSignatureScreen(): ScreenModel

    suspend fun getSignatureScreen(document: String): ScreenModel

    suspend fun searchDocument(document: String)

    suspend fun registerSignature(document: String, signature: String)
}
