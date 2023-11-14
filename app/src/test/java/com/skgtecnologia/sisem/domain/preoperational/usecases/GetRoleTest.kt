package com.skgtecnologia.sisem.domain.preoperational.usecases

import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.preoperational.PreOperationalRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetRoleTest {

    @MockK
    private lateinit var preOperationalRepository: PreOperationalRepository

    private lateinit var getRole: GetRole

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getRole = GetRole(preOperationalRepository)
    }

    @Test
    fun `when getRole is success`() = runTest {
        coEvery { preOperationalRepository.getRole() } returns OperationRole.LEAD_APH

        val result = getRole.invoke()

        Assert.assertEquals(OperationRole.LEAD_APH, result.getOrNull())
    }
}
