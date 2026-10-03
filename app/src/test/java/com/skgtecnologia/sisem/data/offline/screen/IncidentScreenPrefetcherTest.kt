package com.skgtecnologia.sisem.data.offline.screen

import com.skgtecnologia.sisem.commons.connectivity.Connectivity
import com.skgtecnologia.sisem.commons.connectivity.NetworkMonitor
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.incident.IncidentRepository
import com.skgtecnologia.sisem.domain.medicalhistory.MedicalHistoryRepository
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.stretcherretention.StretcherRetentionRepository
import com.valkiria.uicomponents.components.incident.model.IncidentDetailUiModel
import com.valkiria.uicomponents.components.incident.model.IncidentTypeUiModel
import com.valkiria.uicomponents.components.incident.model.IncidentUiModel
import com.valkiria.uicomponents.components.incident.model.PatientUiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.IOException

class IncidentScreenPrefetcherTest {

    private val activeIncident = MutableStateFlow<IncidentUiModel?>(null)
    private val connectivity = MutableStateFlow(Connectivity.ONLINE)

    private val incidentRepository = mockk<IncidentRepository>(relaxed = true) {
        every { observeActiveIncident() } returns activeIncident
    }
    private val medicalHistoryRepository = mockk<MedicalHistoryRepository>(relaxed = true)
    private val stretcherRetentionRepository = mockk<StretcherRetentionRepository>(relaxed = true)
    private val androidIdProvider = mockk<AndroidIdProvider> { every { getAndroidId() } returns "serial-1" }
    private val networkMonitor = object : NetworkMonitor {
        override val connectivity: StateFlow<Connectivity> = this@IncidentScreenPrefetcherTest.connectivity
    }

    private val prefetcher = IncidentScreenPrefetcher(
        incidentRepository,
        medicalHistoryRepository,
        stretcherRetentionRepository,
        androidIdProvider,
        networkMonitor
    )

    private fun incident(vararg patients: PatientUiModel, isActive: Boolean = true) = IncidentUiModel(
        incident = IncidentDetailUiModel(
            id = 7,
            code = "C-7",
            codeSisem = "S-7",
            address = "Calle 1",
            addressReferencePoint = "",
            premierOneDate = "",
            premierOneHour = "",
            incidentType = IncidentTypeUiModel(id = 1, code = "T"),
            doctorAuthName = ""
        ),
        patients = patients.toList(),
        resources = emptyList(),
        isActive = isActive
    )

    private fun patient(idAph: Int, isPendingAph: Boolean = true) =
        PatientUiModel(id = idAph, fullName = "P$idAph", idAph = idAph, isPendingAph = isPendingAph)

    @Test
    fun `an assigned incident has its forms loaded while there is signal`() = runTest(UnconfinedTestDispatcher()) {
        prefetcher.start(backgroundScope)

        activeIncident.value = incident(patient(11), patient(12, isPendingAph = false))

        coVerify(exactly = 1) { incidentRepository.getIncidentScreen() }
        coVerify(exactly = 1) { medicalHistoryRepository.getVitalSignsScreen() }
        coVerify(exactly = 1) { medicalHistoryRepository.getMedicineScreen("serial-1") }
        coVerify(exactly = 1) { stretcherRetentionRepository.getPreStretcherRetentionScreen() }
        coVerify(exactly = 1) { medicalHistoryRepository.getMedicalHistoryScreen("11") }
        coVerify(exactly = 1) { stretcherRetentionRepository.getStretcherRetentionScreen("11") }
        // A history already sent needs no form.
        coVerify(exactly = 0) { medicalHistoryRepository.getMedicalHistoryScreen("12") }
    }

    @Test
    fun `without signal nothing is tried until it comes back`() = runTest(UnconfinedTestDispatcher()) {
        connectivity.value = Connectivity.OFFLINE
        prefetcher.start(backgroundScope)
        activeIncident.value = incident(patient(11))

        coVerify(exactly = 0) { medicalHistoryRepository.getMedicalHistoryScreen(any()) }

        connectivity.value = Connectivity.ONLINE

        coVerify(exactly = 1) { medicalHistoryRepository.getMedicalHistoryScreen("11") }
    }

    @Test
    fun `what failed is retried on reconnection and what loaded is not fetched again`() =
        runTest(UnconfinedTestDispatcher()) {
            coEvery { medicalHistoryRepository.getMedicalHistoryScreen("11") } throws
                IOException("drop") andThen mockk<ScreenModel>()
            prefetcher.start(backgroundScope)
            activeIncident.value = incident(patient(11))

            connectivity.value = Connectivity.OFFLINE
            connectivity.value = Connectivity.ONLINE

            coVerify(exactly = 2) { medicalHistoryRepository.getMedicalHistoryScreen("11") }
            coVerify(exactly = 1) { medicalHistoryRepository.getVitalSignsScreen() }
        }

    @Test
    fun `a patient added to the incident gets their form loaded`() = runTest(UnconfinedTestDispatcher()) {
        prefetcher.start(backgroundScope)
        activeIncident.value = incident(patient(11))

        activeIncident.value = incident(patient(11), patient(13))

        coVerify(exactly = 1) { medicalHistoryRepository.getMedicalHistoryScreen("13") }
    }

    @Test
    fun `a closed incident loads nothing`() = runTest(UnconfinedTestDispatcher()) {
        prefetcher.start(backgroundScope)

        activeIncident.value = incident(patient(11), isActive = false)

        coVerify(exactly = 0) { incidentRepository.getIncidentScreen() }
    }
}
