package com.skgtecnologia.sisem.data.notification

import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.incident.cache.IncidentCacheDataSource
import com.skgtecnologia.sisem.data.incident.remote.IncidentRemoteDataSource
import com.skgtecnologia.sisem.data.notification.cache.NotificationCacheDataSource
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.data.operation.remote.OperationRemoteDataSource
import com.valkiria.uicomponents.bricks.notification.model.IpsPatientTransferredNotification
import com.valkiria.uicomponents.bricks.notification.model.TransmilenioAuthorizationNotification
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NotificationRepositoryImplTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK private lateinit var androidIdProvider: AndroidIdProvider

    @MockK private lateinit var authCacheDataSource: AuthCacheDataSource

    @MockK private lateinit var incidentCacheDataSource: IncidentCacheDataSource

    @MockK private lateinit var incidentRemoteDataSource: IncidentRemoteDataSource

    @MockK private lateinit var notificationCacheDataSource: NotificationCacheDataSource

    @MockK private lateinit var operationCacheDataSource: OperationCacheDataSource

    @MockK private lateinit var operationRemoteDataSource: OperationRemoteDataSource

    private lateinit var repository: NotificationRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        repository = NotificationRepositoryImpl(
            androidIdProvider,
            authCacheDataSource,
            incidentCacheDataSource,
            incidentRemoteDataSource,
            notificationCacheDataSource,
            operationCacheDataSource,
            operationRemoteDataSource
        )
    }

    @Test
    fun `handleIpsPatientTransferredNotification with null geolocation does not crash`() = runTest {
        val notification = mockk<IpsPatientTransferredNotification>(relaxed = true) {
            every { geolocation } returns null
        }
        coEvery { notificationCacheDataSource.storeNotification(any()) } returns 1L

        repository.storeNotification(notification)

        coVerify(exactly = 0) { incidentCacheDataSource.storeIncident(any()) }
    }

    @Test
    fun `handleIpsPatientTransferredNotification with null active incident does not crash`() = runTest {
        val notification = mockk<IpsPatientTransferredNotification>(relaxed = true) {
            every { geolocation } returns "-74.08,4.60"
        }
        coEvery { notificationCacheDataSource.storeNotification(any()) } returns 1L
        coEvery { incidentCacheDataSource.observeActiveIncident() } returns flowOf(null)

        repository.storeNotification(notification)

        coVerify(exactly = 0) { incidentCacheDataSource.storeIncident(any()) }
    }

    @Test
    fun `handleTransmiNotification with null active incident does not crash`() = runTest {
        val notification = mockk<TransmilenioAuthorizationNotification>(relaxed = true)
        coEvery { notificationCacheDataSource.storeNotification(any()) } returns 1L
        coEvery { incidentCacheDataSource.observeActiveIncident() } returns flowOf(null)

        repository.storeNotification(notification)

        coVerify(exactly = 0) { incidentCacheDataSource.updateTransmiStatus(any(), any()) }
    }
}
