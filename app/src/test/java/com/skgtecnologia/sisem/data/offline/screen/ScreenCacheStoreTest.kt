package com.skgtecnologia.sisem.data.offline.screen

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

private const val HOUR = 60 * 60 * 1000L
private const val KEY = "abc123"

class ScreenCacheStoreTest {

    @get:Rule
    val folder = TemporaryFolder()

    private var clock = 1_000_000_000L
    private lateinit var directory: File
    private lateinit var store: ScreenCacheStore

    @Before
    fun setup() {
        directory = folder.newFolder("screens")
        store = ScreenCacheStore(directory, ReversingCipher()) { clock }
    }

    @Test
    fun `a stored screen comes back as it was`() {
        val body = """{"body":[{"type":"LABEL"}]}""".toByteArray()

        store.put(KEY, body)

        assertArrayEquals(body, store.get(KEY))
    }

    @Test
    fun `nothing is written to disk in the clear`() {
        val body = """{"patient":"Juan Perez"}""".toByteArray()

        store.put(KEY, body)

        val onDisk = directory.listFiles()!!.single().readBytes().toString(Charsets.ISO_8859_1)
        assertFalse(onDisk.contains("Juan Perez"))
    }

    @Test
    fun `storing again replaces the previous copy`() {
        store.put(KEY, "old".toByteArray())
        store.put(KEY, "new".toByteArray())

        assertArrayEquals("new".toByteArray(), store.get(KEY))
        assertEquals(1, directory.listFiles()!!.size)
    }

    @Test
    fun `a copy within the retention window is served`() {
        store.put(KEY, "screen".toByteArray())

        clock += 71 * HOUR

        assertArrayEquals("screen".toByteArray(), store.get(KEY))
    }

    @Test
    fun `an expired copy is not served and is deleted`() {
        store.put(KEY, "screen".toByteArray())

        clock += 73 * HOUR

        assertNull(store.get(KEY))
        assertTrue(directory.listFiles()!!.isEmpty())
    }

    @Test
    fun `a copy that can no longer be decrypted is dropped instead of failing every time`() {
        val lostKeyStore = ScreenCacheStore(directory, LostKeyCipher()) { clock }
        lostKeyStore.put(KEY, "screen".toByteArray())

        assertNull(lostKeyStore.get(KEY))
        assertTrue(directory.listFiles()!!.isEmpty())
    }

    @Test
    fun `purging removes old patient data and keeps the rest`() {
        store.put("old", "old".toByteArray())
        clock += 50 * HOUR
        store.put("fresh", "fresh".toByteArray())
        clock += 30 * HOUR

        store.purgeExpired()

        val remaining = directory.listFiles()!!.map { it.nameWithoutExtension }
        assertEquals(listOf("fresh"), remaining)
    }

    @Test
    fun `an unknown key is simply a miss`() {
        assertNull(store.get("never-stored"))
    }
}
