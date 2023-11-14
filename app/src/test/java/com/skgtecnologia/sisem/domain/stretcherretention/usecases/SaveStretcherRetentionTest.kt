package com.skgtecnologia.sisem.domain.stretcherretention.usecases

import com.skgtecnologia.sisem.domain.stretcherretention.StretcherRetentionRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SaveStretcherRetentionTest {

    @MockK
    private lateinit var stretcherRetentionRepository: StretcherRetentionRepository

    private lateinit var saveStretcherRetention: SaveStretcherRetention

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        saveStretcherRetention = SaveStretcherRetention(stretcherRetentionRepository)
    }

    @Test
    fun `when saveStretcherRetention is success`() = runTest {
        coEvery { stretcherRetentionRepository.saveStretcherRetention(any(), any()) } returns Unit

        val result = saveStretcherRetention.invoke(emptyMap(), emptyMap())

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when saveStretcherRetention is failure`() = runTest {
        coEvery {
            stretcherRetentionRepository.saveStretcherRetention(
                any(),
                any()
            )
        } throws Exception()

        val result = saveStretcherRetention.invoke(emptyMap(), emptyMap())

        Assert.assertEquals(true, result.isFailure)
    }
}
