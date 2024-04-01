package com.valkiria.uicomponents.components.incident

import android.graphics.Color.parseColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.bricks.notification.model.TransmiNotification
import com.valkiria.uicomponents.bricks.notification.model.TransmilenioAuthorizationNotification
import com.valkiria.uicomponents.bricks.notification.model.TransmilenioDeniedNotification
import com.valkiria.uicomponents.components.incident.model.IncidentPriority
import com.valkiria.uicomponents.components.incident.model.IncidentUiModel
import com.valkiria.uicomponents.components.incident.model.PatientUiModel
import com.valkiria.uicomponents.components.incident.model.ResourceUiModel
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.toTextStyle
import com.valkiria.uicomponents.mocks.getIncidentAssignedModel
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.TimeUtils.getLocalDateFromUTC
import com.valkiria.uicomponents.utlis.TimeUtils.getLocalTimeAsString
import com.valkiria.uicomponents.utlis.getResourceIdByName
import timber.log.Timber

private val ContentBackground = Modifier.background(color = Color(parseColor("#2B3139")))

@Composable
fun IncidentContent(
    incidentUiModel: IncidentUiModel,
    onAction: (idAph: Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
    ) {
        item(key = "INCIDENT_HEADER") {
            IncidentHeader(
                incidentUiModel.incidentPriority,
                incidentUiModel.incident.codeSisem,
                incidentUiModel.incident.incidentType.code
            )
        }

        item(key = "INCIDENT_DETAILS") {
            IncidentDetails(
                incidentUiModel.incident.address,
                incidentUiModel.incident.premierOneDate,
                incidentUiModel.incident.premierOneHour
            )
        }

        item(key = "INCIDENT_LOCATION") {
            IncidentLocationDescription(incidentUiModel.incident.addressReferencePoint)
        }

        item(key = "INCIDENT_RESOURCES") {
            IncidentResources(incidentUiModel.resources)
        }

        incidentUiModel.transmiRequests?.let { transmiNotifications ->
            item(key = "INCIDENT_TRANSMI_REQUESTS") {
                IncidentTransmilenioRequest(transmiNotifications)
            }
        }

        item(key = "INCIDENT_PATIENTS") {
            IncidentFooter(incidentUiModel.patients, onAction)
        }
    }
}

@Composable
private fun IncidentHeader(incidentPriority: IncidentPriority?, codeSisem: String, code: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .padding(8.dp)
                .size(10.dp),
            painter = painterResource(id = R.drawable.ic_circle),
            tint = incidentPriority?.color ?: IncidentPriority.MEDIUM.color,
            contentDescription = null
        )

        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = "$codeSisem  |",
            color = Color.White,
            style = TextStyle.HEADLINE_2.toTextStyle().copy(
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
        )

        Text(
            text = code,
            color = Color(parseColor("#DB095C")),
            style = TextStyle.HEADLINE_2.toTextStyle().copy(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Suppress("MagicNumber")
@Composable
private fun IncidentDetails(address: String, premierOneDate: String, premierOneHour: String) {
    Row {
        Row(
            modifier = Modifier
                .weight(1.5f)
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painterResource(id = R.drawable.ic_location),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )

            Text(
                modifier = Modifier.padding(end = 8.dp),
                text = address,
                color = Color.White,
                maxLines = 3,
                style = TextStyle.HEADLINE_4.toTextStyle().copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(28.dp)
                    .padding(2.dp),
                painter = painterResource(id = R.drawable.ic_chronometer),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )

            Text(
                text = getLocalDateFromUTC(premierOneDate)
                    .plus("\n")
                    .plus(getLocalTimeAsString(premierOneHour)),
                color = Color.White,
                style = TextStyle.HEADLINE_4.toTextStyle().copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

@Composable
private fun IncidentLocationDescription(addressReferencePoint: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clip(
                shape = RoundedCornerShape(20.dp)
            )
            .then(ContentBackground)
            .padding(10.dp)
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = addressReferencePoint,
            color = Color.White,
            style = TextStyle.HEADLINE_7.toTextStyle().copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        )
    }
}

@Suppress("MagicNumber")
@Composable
private fun IncidentResources(resources: List<ResourceUiModel>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .clip(
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomEnd = 20.dp,
                    bottomStart = 20.dp
                )
            )
            .then(ContentBackground)
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(start = 10.dp, top = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(28.dp),
                painter = painterResource(id = R.drawable.ic_support),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )

            Text(
                text = stringResource(R.string.incident_support_request_title),
                color = Color.White,
                style = TextStyle.HEADLINE_4.toTextStyle().copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        resources.forEach { resource ->
            IncidentResource(resource)
        }
    }
}

@Composable
private fun IncidentResource(resource: ResourceUiModel) {
    Row(
        modifier = Modifier
            .padding(start = 40.dp, top = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val iconResourceId = LocalContext.current.getResourceIdByName(
            resource.resource.icTransitAgency, DefType.DRAWABLE
        )

        iconResourceId?.let {
            Icon(
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(20.dp),
                painter = painterResource(id = iconResourceId),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
        }

        Text(
            text = resource.resource.name,
            color = Color.White,
            style = TextStyle.HEADLINE_3.toTextStyle().copy(
                fontSize = 14.sp,
                fontWeight = if (resource.resource.icTransitAgency == "ic_ambulance") {
                    FontWeight.SemiBold
                } else {
                    FontWeight.Normal
                }
            )
        )
    }
}

@Suppress("LongMethod", "MagicNumber")
@Composable
private fun IncidentTransmilenioRequest(transmiRequests: List<TransmiNotification>) {
    transmiRequests.forEach { transmiNotification ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomEnd = 20.dp,
                        bottomStart = 20.dp
                    )
                )
                .then(ContentBackground)
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .size(20.dp),
                        painter = painterResource(id = R.drawable.ic_road),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )

                    Text(
                        text = stringResource(R.string.incident_transmi_lane_request_title),
                        color = Color.White,
                        style = TextStyle.HEADLINE_4.toTextStyle()
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(start = 26.dp, end = 8.dp),
                        text = when (transmiNotification) {
                            is TransmilenioAuthorizationNotification -> stringResource(
                                id = R.string.incident_transmi_auth_number,
                                transmiNotification.authorizationNumber
                            )

                            is TransmilenioDeniedNotification -> stringResource(
                                id = R.string.incident_transmi_auth_number,
                                transmiNotification.authorizationNumber
                            )

                            else -> error("Invalid TransmiNotification")
                        },
                        color = Color.White,
                        style = TextStyle.HEADLINE_6.toTextStyle().copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    if (transmiNotification is TransmilenioDeniedNotification) {
                        Divider(
                            color = Color.White,
                            modifier = Modifier
                                .height(25.dp)
                                .width(1.dp)
                        )

                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = stringResource(id = R.string.incident_transmi_auth_denied),
                            color = Color(parseColor("#F55757")),
                            style = TextStyle.HEADLINE_6.toTextStyle().copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                }

                if (transmiNotification is TransmilenioAuthorizationNotification) {
                    Text(
                        modifier = Modifier.padding(start = 26.dp),
                        text = stringResource(
                            id = R.string.incident_transmi_authorizes,
                            transmiNotification.authorizes
                        ),
                        color = Color.White,
                        style = TextStyle.HEADLINE_6.toTextStyle(),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )

                    Row(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(start = 26.dp, end = 2.dp)
                                .fillMaxWidth(0.60f),
                            text = stringResource(
                                id = R.string.incident_transmi_journey,
                                transmiNotification.journey
                            ),
                            color = Color.White,
                            style = TextStyle.HEADLINE_6.toTextStyle()
                        )

                        Divider(
                            color = Color.White,
                            modifier = Modifier
                                .height(25.dp)
                                .width(1.dp)
                        )

                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = stringResource(id = R.string.incident_transmi_auth_approved),
                            color = Color(parseColor("#3CF2DD")),
                            style = TextStyle.HEADLINE_6.toTextStyle().copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun IncidentFooter(patients: List<PatientUiModel>, onAction: (idAph: Int) -> Unit) {
    Column(
        modifier = Modifier
            .padding(top = 20.dp, bottom = 20.dp)
            .fillMaxWidth()
    ) {
        patients.forEach { patientUiModel ->
            IncidentPatient(patientUiModel, onAction)
        }
    }
}

@Composable
private fun IncidentPatient(patientUiModel: PatientUiModel, onAction: (idAph: Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Button(
            onClick = { onAction(patientUiModel.idAph) },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Icon(
                modifier = Modifier.padding(end = 10.dp),
                painter = painterResource(id = R.drawable.ic_patient),
                contentDescription = null
            )
            Text(
                text = patientUiModel.fullName,
                color = Color(parseColor("#0A090A")),
                style = TextStyle.HEADLINE_6.toTextStyle().copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        Button(
            onClick = { /*TODO*/ }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_email),
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun IncidentContentPreview() {
    IncidentContent(getIncidentAssignedModel()) {
        Timber.d("Footer action clicked")
    }
}
