package com.valkiria.uicomponents.bricks.notification

import android.graphics.Color.parseColor
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.R.drawable
import com.valkiria.uicomponents.action.GenericUiAction.NotificationAction
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.INCIDENT_ASSIGNED
import com.valkiria.uicomponents.mocks.getAssignedIncidentNotificationUiModel
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName
import kotlinx.coroutines.delay
import timber.log.Timber

const val DISMISS_DELAY = 800L

@androidx.compose.material3.ExperimentalMaterial3Api
@Composable
fun NotificationView(
    uiModel: NotificationUiModel,
    onAction: (notificationAction: NotificationAction) -> Unit
) {
    var show by remember { mutableStateOf(true) }
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart ||
                it == SwipeToDismissBoxValue.StartToEnd
            ) {
                show = false
                true
            } else false
        }
    )

    LaunchedEffect(show) {
        if (!show) {
            Timber.d("Notification handled")
            delay(DISMISS_DELAY)
            onAction(NotificationAction(identifier = uiModel.identifier, isDismiss = true))
        }
    }

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
        AnimatedVisibility(
            show, exit = fadeOut(spring())
        ) {
            SwipeToDismissBox(
                state = dismissState,
                modifier = Modifier,
                backgroundContent = {
                    Color.Transparent
                }
            ) {
                NotificationViewRender(uiModel, iconResourceId, onAction)
            }
        }
    }
}

@Suppress("ComplexMethod", "LongMethod")
@Composable
private fun NotificationViewRender(
    uiModel: NotificationUiModel,
    iconResourceId: Int?,
    onAction: (notificationAction: NotificationAction) -> Unit
) {
    val isNotIncidentAssigned = remember {
        uiModel.title != INCIDENT_ASSIGNED.title
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(color = Color.Black, shape = RoundedCornerShape(20.dp))
            .border(
                BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                RoundedCornerShape(20.dp)
            )
            .clickable(
                onClick = {
                    onAction(
                        NotificationAction(
                            identifier = uiModel.identifier,
                            isDismiss = false
                        )
                    )
                }
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom
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
                    style = MaterialTheme.typography.headlineMedium
                )

                if (isNotIncidentAssigned && uiModel.description == null &&
                    uiModel.content == null
                ) {
                    Text(
                        text = uiModel.timeStamp,
                        modifier = Modifier.wrapContentWidth(),
                        color = Color(parseColor("#AFAFAF")),
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            uiModel.description?.let {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = uiModel.description,
                        modifier = if (uiModel.title == INCIDENT_ASSIGNED.title) {
                            Modifier
                                .padding(start = 48.dp)
                                .weight(1f)
                        } else {
                            Modifier
                                .padding(start = 42.dp)
                                .weight(1f)
                        },
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    if (isNotIncidentAssigned && uiModel.content == null) {
                        Text(
                            text = uiModel.timeStamp,
                            modifier = Modifier.wrapContentWidth(),
                            color = Color(parseColor("#AFAFAF")),
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            uiModel.content?.let {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.Bottom
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
                            Modifier
                                .padding(start = 4.dp)
                                .weight(1f)
                        } else {
                            Modifier
                                .padding(start = 42.dp)
                                .weight(1f)
                        },
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    if (isNotIncidentAssigned) {
                        Text(
                            text = uiModel.timeStamp,
                            modifier = Modifier.wrapContentWidth(),
                            color = Color(parseColor("#AFAFAF")),
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
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
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationViewPreview() {
    Column(modifier = Modifier.background(Color.White)) {
        NotificationView(
            uiModel = getAssignedIncidentNotificationUiModel(),
            onAction = { identifier -> Timber.d("Closed $identifier") }
        )
    }
}
