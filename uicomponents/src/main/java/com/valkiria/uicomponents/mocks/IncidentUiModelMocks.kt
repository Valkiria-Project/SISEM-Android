package com.valkiria.uicomponents.mocks

import com.valkiria.uicomponents.bricks.notification.model.TransmilenioAuthorizationNotification
import com.valkiria.uicomponents.components.incident.model.IncidentDetailUiModel
import com.valkiria.uicomponents.components.incident.model.IncidentPriority
import com.valkiria.uicomponents.components.incident.model.IncidentTypeUiModel
import com.valkiria.uicomponents.components.incident.model.IncidentUiModel
import com.valkiria.uicomponents.components.incident.model.PatientUiModel
import com.valkiria.uicomponents.components.incident.model.ResourceDetailUiModel
import com.valkiria.uicomponents.components.incident.model.ResourceUiModel
import java.time.LocalDateTime
import java.time.LocalTime

@Suppress("LongMethod")
fun getIncidentAssignedModel(): IncidentUiModel {
    return IncidentUiModel(
        id = 1,
        incident = IncidentDetailUiModel(
            id = 27,
            code = "00122305",
            codeSisem = "CRU-12345678-24",
            address = "Av Ciudad de Cali #11-1, Bogotá, Colombia",
            addressReferencePoint =
            "Av Ciudad de Cali #11-1, Bogotá, Colombia",
            premierOneDate = "2023-12-01T00:00:00",
            premierOneHour = "16:34:48",
            incidentType = IncidentTypeUiModel(
                id = 1,
                code = "906"
            ),
            doctorAuthName = "Ricardo Grajales"
        ),
        patients = listOf(
            PatientUiModel(
                id = 49,
                idAph = 110,
                fullName = "Carolina Restrepo",
                disabled = false
            ),
            PatientUiModel(
                id = 50,
                idAph = 111,
                fullName = "Luis Villada",
                disabled = true
            )
        ),
        resources = listOf(
            ResourceUiModel(
                id = 237,
                resourceId = 359,
                resource = ResourceDetailUiModel(
                    id = 359,
                    code = "00122305",
                    name = "Policía",
                    icTransitAgency = "ic_police"
                )
            ),
            ResourceUiModel(
                id = 238,
                resourceId = 360,
                resource = ResourceDetailUiModel(
                    id = 360,
                    code = "00122305",
                    name = "Bomberos",
                    icTransitAgency = "ic_firefighter"
                )
            )
        ),
        incidentPriority = IncidentPriority.LOW,
        transmiRequests = listOf(
            TransmilenioAuthorizationNotification(
                time = LocalTime.now(),
                dateTime = LocalDateTime.now(),
                authorizationNumber = "123",
                authorizes = "Funcionario de Transmilenio",
                journey = "Trayecto: campo abierto, info enviada desde sise web: dirección o geolocation"
            )
        )
    )
}
