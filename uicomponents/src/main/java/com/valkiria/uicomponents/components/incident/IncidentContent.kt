package com.valkiria.uicomponents.components.incident

import android.graphics.Color.parseColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.components.incident.model.IncidentUiModel
import com.valkiria.uicomponents.components.incident.model.ResourceUiModel
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.toTextStyle

private val ContentBackground = Modifier.background(color = Color(parseColor("#2B3139")))

@Composable
fun IncidentContent(incidentUiModel: IncidentUiModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
    ) {
        IncidentHeader(
            incidentUiModel.incident.codeSisem,
            incidentUiModel.incident.incidentType.code
        )

        IncidentDetails(
            incidentUiModel.incident.address,
            incidentUiModel.incident.premierOneDate,
            incidentUiModel.incident.premierOneHour
        )

        IncidentLocationDescription(incidentUiModel.incident.addressReferencePoint)

        IncidentResources(incidentUiModel.resources)

        TransmilenioLane()

        IncidentPart5()
    }
}

@Composable
private fun IncidentHeader(codeSisem: String, code: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .padding(8.dp)
                .size(10.dp),
            painter = painterResource(id = R.drawable.ic_circle),
            tint = Color(parseColor("#BE392F")),
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

@Composable
private fun IncidentDetails(address: String, premierOneDate: String, premierOneHour: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
            style = TextStyle.HEADLINE_4.toTextStyle().copy(
                fontWeight = FontWeight.SemiBold
            )
        )

        Icon(
            modifier = Modifier
                .padding(end = 6.dp)
                .size(28.dp),
            painter = painterResource(id = R.drawable.ic_chronometer),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null
        )

        Text(
            text = premierOneDate.plus(" ").plus(premierOneHour),
            color = Color.White,
            style = TextStyle.HEADLINE_4.toTextStyle().copy(
                fontWeight = FontWeight.SemiBold
            )
        )
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
            style = TextStyle.HEADLINE_7.toTextStyle()
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
                style = TextStyle.HEADLINE_4.toTextStyle()
            )
        }

        resources.forEach { _ ->
            IncidentResource()
        }
    }
}

@Composable
private fun IncidentResource() {
    Row(
        modifier = Modifier
            .padding(start = 46.dp, top = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .padding(end = 6.dp)
                .size(28.dp),
            painter = painterResource(id = R.drawable.ic_location),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null
        )

        Text(
            text = "No 4394",
            color = Color.White,
            style = TextStyle.HEADLINE_6.toTextStyle()
        )
    }
}

@Suppress("LongMethod", "MagicNumber")
@Composable
private fun TransmilenioLane() {
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
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
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
                    text = "Carril de transmilenio",
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
                    modifier = Modifier.padding(start = 26.dp, end = 6.dp),
                    text = "No 987798",
                    color = Color.White,
                    style = TextStyle.HEADLINE_6.toTextStyle()
                )

                Text(
                    text = "|",
                    style = TextStyle.HEADLINE_4.toTextStyle(),
                    color = Color.White,
                )

                Text(
                    modifier = Modifier.padding(start = 2.dp),
                    text = "No hay respuesta",
                    color = Color.White,
                    style = TextStyle.HEADLINE_6.toTextStyle(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }

            Text(
                modifier = Modifier.padding(start = 26.dp),
                text = "Autoriza: Juan Correa algo",
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
                    text = "Trayecto Perdomo a Reicaurte",
                    color = Color.White,
                    style = TextStyle.HEADLINE_6.toTextStyle()
                )

                Text(
                    text = "|",
                    style = TextStyle.HEADLINE_3.toTextStyle(),
                    color = Color.White,
                )

                Text(
                    modifier = Modifier.padding(start = 2.dp),
                    text = "Autorizado",
                    color = Color(parseColor("#3CF2DD")),
                    style = TextStyle.HEADLINE_6.toTextStyle(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun IncidentPart5() {
    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
    ) {
        IncidentPart5_1()
    }
}

@Composable
private fun IncidentPart5_1() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Button(
            onClick = { /*TODO*/ }
        ) {
            Icon(
                modifier = Modifier.padding(end = 6.dp),
                painter = painterResource(id = R.drawable.ic_hceud),
                contentDescription = null
            )
            Text(
                text = "Gabriela Quintero",
                color = Color.White,
                style = TextStyle.HEADLINE_6.toTextStyle()
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
    // FIXME: Create mock
//    IncidentContent(it)
}
