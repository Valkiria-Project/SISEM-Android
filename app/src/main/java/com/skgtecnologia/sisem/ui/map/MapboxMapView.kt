package com.skgtecnologia.sisem.ui.map

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.fragment.compose.AndroidFragment
import androidx.fragment.compose.FragmentState
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.action.GenericUiAction.NotificationAction
import com.valkiria.uicomponents.bricks.banner.BannerUiModel
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.notification.NotificationRowView
import com.valkiria.uicomponents.bricks.notification.NotificationUiModel
import com.valkiria.uicomponents.bricks.notification.OnNotificationHandler
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import com.valkiria.uicomponents.components.incident.IncidentContent
import com.valkiria.uicomponents.components.incident.model.IncidentUiModel
import kotlinx.coroutines.launch
import timber.log.Timber

@Suppress("LongMethod", "LongParameterList", "MagicNumber")
@Composable
fun MapboxMapView(
    incident: IncidentUiModel?,
    notifications: List<NotificationUiModel>?,
    drawerState: DrawerState,
    notificationData: NotificationData?,
    incidentErrorData: BannerUiModel?,
    modifier: Modifier = Modifier,
    fragmentState: FragmentState,
    onNotificationAction: (notificationAction: NotificationAction) -> Unit,
    onIncidentErrorAction: () -> Unit,
    onAction: (idAph: Int) -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    var showNotificationsDialog by remember { mutableStateOf(false) }

    val currentIncident by remember(incident) { mutableStateOf(incident) }

    BottomSheetScaffold(
        sheetContent = {
            currentIncident?.let {
                IncidentContent(incidentUiModel = it, onAction = onAction)
            }
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = if (currentIncident != null) 140.dp else 0.dp,
        sheetSwipeEnabled = true
    ) { innerPadding ->
        Box(modifier.padding(innerPadding)) {
            AndroidFragment<MapFragment>(
                fragmentState = fragmentState
            )

            IconButton(
                onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                },
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Outlined.Menu,
                    contentDescription = null,
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.primary)
                        .size(72.dp)
                        .padding(5.dp),
                    tint = Color.Black
                )
            }

            IconButton(
                onClick = {
                    showNotificationsDialog = true
                },
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Outlined.Notifications,
                    contentDescription = null,
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.primary)
                        .size(72.dp)
                        .padding(5.dp),
                    tint = Color.Black
                )
            }

            OnNotificationHandler(notificationData) {
                onNotificationAction(it)
                if (it.isDismiss.not()) {
                    Timber.d("Navigate to MapScreen")
                }
            }

            NotificationsRenderer(showNotificationsDialog, notifications) {
                showNotificationsDialog = false
            }

            OnBannerHandler(uiModel = incidentErrorData) {
                onIncidentErrorAction()
            }
        }
    }
}

@Composable
private fun NotificationsRenderer(
    showNotificationsDialog: Boolean,
    notifications: List<NotificationUiModel>?,
    onAction: () -> Unit
) {
    if (showNotificationsDialog) {
        Dialog(
            onDismissRequest = { onAction() },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
                    .clickable(
                        enabled = false,
                        onClick = {}
                    )
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_back),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable { onAction() }
                            .padding(20.dp)
                            .size(32.dp),
                        tint = Color.White
                    )

                    Text(
                        text = stringResource(R.string.notifications_title),
                        modifier = Modifier
                            .weight(1f)
                            .padding(20.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                notifications?.forEach { notificationUiModel ->
                    NotificationRowView(notificationUiModel)
                }
            }
        }
    }
}
