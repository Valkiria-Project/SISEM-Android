package com.skgtecnologia.sisem.data.offline.screen

import com.skgtecnologia.sisem.commons.security.BlobCipher

/**
 * Stands in for the Keystore cipher, which does not exist on the JVM. It really transforms the
 * bytes, so a test can tell that nothing reaches the disk in the clear.
 */
internal class ReversingCipher : BlobCipher {
    override fun encrypt(plaintext: ByteArray): ByteArray = byteArrayOf(SEAL) + plaintext.reversedArray()

    override fun decrypt(sealed: ByteArray): ByteArray {
        check(sealed.firstOrNull() == SEAL) { "not sealed by this cipher" }
        return sealed.copyOfRange(1, sealed.size).reversedArray()
    }

    private companion object {
        const val SEAL: Byte = 0x5A
    }
}

/** A key that is gone: what is left after app data is cleared or a device is restored. */
internal class LostKeyCipher : BlobCipher {
    override fun encrypt(plaintext: ByteArray): ByteArray = plaintext

    override fun decrypt(sealed: ByteArray): ByteArray = throw javax.crypto.AEADBadTagException("key gone")
}
