package com.valkiria.uicomponents.bricks.button

import android.graphics.Color.parseColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.toTextStyle
import timber.log.Timber

@Composable
fun ImageButtonView(
    uiModel: ImageButtonUiModel,
    onAction: (id: String) -> Unit
) {
    Column(
        modifier = uiModel.modifier
            .then(
                if (uiModel.backgroundColor != null) {
                    Modifier.background(Color(parseColor(uiModel.backgroundColor)))
                } else {
                    Modifier
                }
            )
            .padding(12.dp)
            .clickable { onAction(uiModel.identifier) },
        horizontalAlignment = uiModel.alignment,
        verticalArrangement = Arrangement.Bottom
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = uiModel.iconResId),
            contentDescription = null,
            modifier = Modifier.size(uiModel.size),
            alignment = Alignment.BottomCenter,
            colorFilter = ColorFilter.tint(Color.White)
        )

        uiModel.label?.let {
            Text(
                text = uiModel.label,
                textAlign = TextAlign.Center,
                style = uiModel.textStyle.toTextStyle()
            )
        }
    }
}

@Preview
@Composable
fun ImageButtonViewPreview() {
    ImageButtonView(
        uiModel = ImageButtonUiModel(
            identifier = "FILE",
            iconResId = R.drawable.ic_file,
            label = stringResource(
                id = R.string.media_action_select_files
            ),
            textStyle = TextStyle.HEADLINE_6,
            size = 72.dp,
            alignment = Alignment.CenterHorizontally,
            backgroundColor = "#3F4145",
            modifier = Modifier
                .padding(8.dp)
        )
    ) {
        Timber.d("no-op")
    }
}
