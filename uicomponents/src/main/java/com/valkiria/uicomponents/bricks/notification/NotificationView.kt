package com.valkiria.uicomponents.bricks.notification

import android.graphics.Color.parseColor
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.mocks.getAssignedIncidentNotificationUiModel
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName
import timber.log.Timber

@Suppress("LongMethod", "MagicNumber")
@Composable
fun NotificationView(
    uiModel: NotificationUiModel,
    onAction: (id: String) -> Unit
) {
    val iconResourceId = LocalContext.current.getResourceIdByName(
        uiModel.icon, DefType.DRAWABLE
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onBackground)
            .clickable(
                enabled = false,
                onClick = { onAction(uiModel.identifier) }
            ),
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomEnd = 20.dp,
                        bottomStart = 20.dp
                    )
                )
                .background(color = Color.Black)
                .border(
                    BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    RoundedCornerShape(14)
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
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(32.dp),
                            tint = Color(parseColor(uiModel.iconColor))
                        )
                    }
                    Text(
                        text = uiModel.title,
                        modifier = Modifier.weight(1f),
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = uiModel.description,
                        modifier = Modifier.padding(start = 48.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_location),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 48.dp)
                            .size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = uiModel.location,
                        modifier = Modifier.padding(start = 4.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_clock_timer),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = uiModel.time,
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
    NotificationView(
        uiModel = getAssignedIncidentNotificationUiModel(),
        onAction = { identifier -> Timber.d("Closed $identifier") }
    )
}
