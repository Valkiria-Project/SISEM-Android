package com.skgtecnologia.sisem.domain.inventory.usecases

import com.skgtecnologia.sisem.commons.SERIAL
import com.skgtecnologia.sisem.domain.inventory.InventoryRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetInventoryInitialScreenTest {

    @MockK
    private lateinit var inventoryRepository: InventoryRepository

    private lateinit var getInventoryInitialScreen: GetInventoryInitialScreen

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getInventoryInitialScreen = GetInventoryInitialScreen(inventoryRepository)
    }

    @Test
    fun `when getInventoryInitialScreen is success`() = runTest {
        coEvery { inventoryRepository.getInventoryInitialScreen(any()) } returns mockk()

        val result = getInventoryInitialScreen.invoke(SERIAL)

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when getInventoryInitialScreen is failure`() = runTest {
        coEvery { inventoryRepository.getInventoryInitialScreen(any()) } throws Exception()

        val result = getInventoryInitialScreen.invoke(SERIAL)

        Assert.assertEquals(true, result.isFailure)
    }
}
