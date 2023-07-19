package com.valkiria.uicomponents.components.errorbanner

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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.mocks.getLoginBlockedErrorUiModel
import com.valkiria.uicomponents.theme.UiComponentsTheme
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName
import timber.log.Timber

@Suppress("LongMethod")
@Composable
fun ErrorBannerComponent(
    uiModel: ErrorUiModel,
    onAction: () -> Unit
) {
    val iconResourceId = LocalContext.current.getResourceIdByName(
        uiModel.icon.orEmpty(), DefType.DRAWABLE
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
            .clickable(
                enabled = false,
                onClick = {}
            ),
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomEnd = 0.dp,
                        bottomStart = 0.dp
                    )
                )
                .background(color = MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.TopCenter
        ) {
            Column {
                Row(
                    modifier = Modifier.padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    iconResourceId?.let {
                        Icon(
                            painter = painterResource(id = it),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(42.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    Text(
                        text = uiModel.title,
                        modifier = Modifier.weight(1f),
                        color = Color.White,
                        style = MaterialTheme.typography.displayLarge
                    )
                    IconButton(
                        onClick = { onAction() },
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(42.dp),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
                Text(
                    text = uiModel.text,
                    modifier = Modifier.padding(20.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorBannerComponentPreview() {
    UiComponentsTheme {
        ErrorBannerComponent(
            uiModel = getLoginBlockedErrorUiModel()
        ) {
            Timber.d("Closed")
        }
    }
}
