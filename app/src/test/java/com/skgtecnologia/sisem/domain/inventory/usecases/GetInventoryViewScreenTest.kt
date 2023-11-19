package com.skgtecnologia.sisem.domain.inventory.usecases

import com.skgtecnologia.sisem.domain.inventory.InventoryRepository
import com.skgtecnologia.sisem.domain.inventory.model.InventoryType
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetInventoryViewScreenTest {

    @MockK
    private lateinit var inventoryRepository: InventoryRepository

    private lateinit var getInventoryViewScreen: GetInventoryViewScreen

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getInventoryViewScreen = GetInventoryViewScreen(inventoryRepository)
    }

    @Test
    fun `when getInventoryViewScreen is success`() = runTest {
        coEvery { inventoryRepository.getInventoryViewScreen(any()) } returns mockk()

        val result = getInventoryViewScreen.invoke(InventoryType.MEDICINE)

        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `when getInventoryViewScreen is failure`() = runTest {
        coEvery { inventoryRepository.getInventoryViewScreen(any()) } throws Exception()

        val result = getInventoryViewScreen.invoke(InventoryType.MEDICINE)

        Assert.assertEquals(true, result.isFailure)
    }
}
