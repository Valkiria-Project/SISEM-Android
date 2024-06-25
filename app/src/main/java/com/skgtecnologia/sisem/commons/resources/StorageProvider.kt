package com.skgtecnologia.sisem.commons.resources

interface StorageProvider {

    fun storeContent(filename: String, mode: Int, content: ByteArray)
}
