package com.skgtecnologia.sisem.data.offline.screen

import com.skgtecnologia.sisem.commons.connectivity.Connectivity
import com.skgtecnologia.sisem.commons.connectivity.NetworkMonitor
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.incident.IncidentRepository
import com.skgtecnologia.sisem.domain.medicalhistory.MedicalHistoryRepository
import com.skgtecnologia.sisem.domain.stretcherretention.StretcherRetentionRepository
import com.valkiria.uicomponents.components.incident.model.IncidentUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

/** The screens an incident needs, identified by the patients' medical histories. */
internal data class PrefetchPlan(val incidentId: Int, val aphIds: List<String>)

internal fun IncidentUiModel.toPrefetchPlan() = PrefetchPlan(
    incidentId = incident.id,
    aphIds = patients.filter { it.isPendingAph && !it.disabled }.map { it.idAph.toString() }
)

/**
 * Loads the forms of the active incident while there is signal, so they are in the screen cache
 * before the crew needs them.
 *
 * Without it, only screens already opened work offline — and an ambulance is assigned an incident
 * on the way out, then loses coverage on arrival, before anyone has opened the medical history.
 *
 * It goes through the same repositories as the screens, so each request — and with it the cache
 * key — is exactly the one the screen will make. Those repositories are bound per ViewModel, so
 * OfflineModule builds this from their implementations. What fails is tried again when the signal comes
 * back; what loaded is not fetched twice for the same incident.
 */
class IncidentScreenPrefetcher(
    private val incidentRepository: IncidentRepository,
    private val medicalHistoryRepository: MedicalHistoryRepository,
    private val stretcherRetentionRepository: StretcherRetentionRepository,
    private val androidIdProvider: AndroidIdProvider,
    private val networkMonitor: NetworkMonitor
) {

    fun start(scope: CoroutineScope): Job = scope.launch {
        val loaded = mutableSetOf<String>()
        var loadedFor: PrefetchPlan? = null

        combine(
            incidentRepository.observeActiveIncident()
                .map { incident -> incident?.takeIf { it.isActive }?.toPrefetchPlan() }
                .distinctUntilChanged(),
            networkMonitor.connectivity
                .map { it != Connectivity.OFFLINE }
                .distinctUntilChanged()
        ) { plan, hasNetwork -> plan.takeIf { hasNetwork } }
            .collectLatest { plan ->
                if (plan == null) return@collectLatest
                if (plan != loadedFor) {
                    loaded.clear()
                    loadedFor = plan
                }

                screensFor(plan)
                    .filterKeys { it !in loaded }
                    .forEach { (name, load) ->
                        resultOf { load() }
                            .onSuccess { loaded += name }
                            .onFailure { Timber.d("Prefetch of $name failed: ${it.message}") }
                    }
            }
    }

    private fun screensFor(plan: PrefetchPlan): Map<String, suspend () -> Unit> = buildMap {
        put("incident") { incidentRepository.getIncidentScreen() }
        put("vital-signs") { medicalHistoryRepository.getVitalSignsScreen() }
        put("medicines") { medicalHistoryRepository.getMedicineScreen(androidIdProvider.getAndroidId()) }
        put("pre-stretcher-retention") { stretcherRetentionRepository.getPreStretcherRetentionScreen() }

        plan.aphIds.forEach { idAph ->
            put("aph:$idAph") { medicalHistoryRepository.getMedicalHistoryScreen(idAph) }
            put("stretcher-retention:$idAph") { stretcherRetentionRepository.getStretcherRetentionScreen(idAph) }
        }
    }
}
