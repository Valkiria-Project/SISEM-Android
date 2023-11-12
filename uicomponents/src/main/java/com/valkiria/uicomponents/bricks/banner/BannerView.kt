package com.valkiria.uicomponents.bricks.banner

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
import androidx.compose.material3.IconButton
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
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.button.ButtonView
import com.valkiria.uicomponents.mocks.getLoginBlockedErrorUiModel
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName
import timber.log.Timber

@Suppress("LongMethod")
@Composable
internal fun BannerView(
    uiModel: BannerUiModel,
    onAction: (actionInput: UiAction) -> Unit
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
                .background(color = Color.DarkGray),
            contentAlignment = Alignment.TopCenter
        ) {
            Column {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    iconResourceId?.let {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = it),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(top = 20.dp, end = 16.dp)
                                .size(42.dp),
                            tint = Color(parseColor(uiModel.iconColor))
                        )
                    }
                    Text(
                        text = uiModel.title,
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .weight(1f),
                        color = Color.White,
                        style = MaterialTheme.typography.displayLarge
                    )
                    if (uiModel.leftButton == null) {
                        IconButton(
                            onClick = { onAction(GenericUiAction.DismissAction) },
                            modifier = Modifier
                                .padding(start = 16.dp, top = 12.dp)
                                .size(42.dp),
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_close),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }
                Text(
                    text = uiModel.description,
                    modifier = Modifier.padding(20.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
                Row(
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        20.dp,
                        Alignment.CenterHorizontally
                    )
                ) {
                    uiModel.leftButton?.let { buttonUiModel ->
                        ButtonView(uiModel = buttonUiModel) {
                            onAction(FooterUiAction.FooterButton(buttonUiModel.identifier))
                        }
                    }

                    uiModel.rightButton?.let { buttonUiModel ->
                        ButtonView(uiModel = buttonUiModel) {
                            onAction(FooterUiAction.FooterButton(buttonUiModel.identifier))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BannerViewPreview() {
    BannerView(
        uiModel = getLoginBlockedErrorUiModel(),
        onAction = { Timber.d("Closed") }
    )
}
