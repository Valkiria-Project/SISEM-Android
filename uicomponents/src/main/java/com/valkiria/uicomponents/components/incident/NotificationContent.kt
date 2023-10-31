package com.valkiria.uicomponents.components.incident

import android.graphics.Color.parseColor
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.toTextStyle

@Composable
fun NotificationContent() {
    val shape = RoundedCornerShape(
        topStart = 20.dp,
        topEnd = 20.dp,
        bottomEnd = 20.dp,
        bottomStart = 20.dp
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = shape)
            .background(Color(parseColor("#141414")))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = shape
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(32.dp),
                    painter = painterResource(
                        id = R.drawable.ic_notification_alert
                    ),
                    contentDescription = null
                )

                Text(
                    text = "Incidente Asignado",
                    color = Color.White,
                    style = TextStyle.HEADLINE_4.toTextStyle()
                )
            }

            Text(
                modifier = Modifier.padding(start = 42.dp, top = 10.dp),
                text = "CRU-12345678-22",
                color = Color.White,
                style = TextStyle.HEADLINE_5.toTextStyle()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 42.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(32.dp),
                    painter = painterResource(
                        id = R.drawable.ic_location
                    ),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    modifier = Modifier.padding(end = 4.dp),
                    text = "Kra 45 #43-21",
                    color = Color.White,
                    style = TextStyle.HEADLINE_6.toTextStyle()
                )

                Icon(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(32.dp),
                    painter = painterResource(
                        id = R.drawable.ic_chronometer
                    ),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    modifier = Modifier.padding(end = 4.dp),
                    text = "11 Min",
                    color = Color.White,
                    style = TextStyle.HEADLINE_6.toTextStyle()
                )
            }
        }
    }
}

@Preview
@Composable
fun NotificationContentPreview() {
    NotificationContent()
}