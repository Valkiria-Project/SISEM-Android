package com.valkiria.uicomponents.bricks.notification

import android.graphics.Color.parseColor
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valkiria.uicomponents.R.drawable
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.INCIDENT_ASSIGNED
import com.valkiria.uicomponents.mocks.getAssignedIncidentNotificationUiModel
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName
import timber.log.Timber

@Composable
fun NotificationRowView(
    uiModel: NotificationUiModel,
) {
    val iconResourceId = LocalContext.current.getResourceIdByName(
        uiModel.icon, DefType.DRAWABLE
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .clickable(
                enabled = false,
                onClick = { Timber.d("no-op") }
            ),
        verticalArrangement = Arrangement.Top
    ) {
        NotificationViewRender(uiModel, iconResourceId)
    }
}

@Suppress("LongMethod")
@Composable
private fun NotificationViewRender(
    uiModel: NotificationUiModel,
    iconResourceId: Int?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(
                color = Color(
                    parseColor("#B23D3F42")
                ),
                shape = RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                iconResourceId?.let {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = it),
                        contentDescription = null,
                        modifier = if (uiModel.title == INCIDENT_ASSIGNED.title) {
                            Modifier
                                .padding(end = 16.dp)
                                .size(32.dp)
                        } else {
                            Modifier
                                .padding(end = 16.dp)
                                .size(24.dp)
                        },
                        tint = Color(parseColor(uiModel.iconColor))
                    )
                }
                Text(
                    text = uiModel.title,
                    modifier = Modifier.weight(1f),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            uiModel.description?.let {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = uiModel.description,
                        modifier = if (uiModel.title == INCIDENT_ASSIGNED.title) {
                            Modifier.padding(start = 48.dp)
                        } else {
                            Modifier.padding(start = 42.dp)
                        },
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            uiModel.content?.let {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (uiModel.title == INCIDENT_ASSIGNED.title) {
                        Icon(
                            imageVector = ImageVector.vectorResource(
                                id = drawable.ic_location
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(start = 48.dp)
                                .size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Text(
                        text = uiModel.content,
                        modifier = if (uiModel.title == INCIDENT_ASSIGNED.title) {
                            Modifier.padding(start = 4.dp)
                        } else {
                            Modifier.padding(start = 42.dp)
                        },
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            if (uiModel.title == INCIDENT_ASSIGNED.title) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            id = drawable.ic_calendar
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 48.dp)
                            .size(24.dp)
                            .padding(2.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = uiModel.date.orEmpty(),
                        modifier = Modifier.padding(start = 4.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Icon(
                        imageVector = ImageVector.vectorResource(
                            id = drawable.ic_clock
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(24.dp)
                            .padding(2.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = uiModel.time.orEmpty(),
                        modifier = Modifier.padding(start = 4.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = uiModel.timeStamp,
                        modifier = Modifier.weight(1f),
                        color = Color(parseColor("#AFAFAF")),
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationRowViewPreview() {
    Column(modifier = Modifier.background(Color.White)) {
        NotificationRowView(
            uiModel = getAssignedIncidentNotificationUiModel()
        )
    }
}
