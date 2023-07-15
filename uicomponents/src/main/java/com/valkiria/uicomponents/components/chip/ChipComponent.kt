package com.valkiria.uicomponents.components.chip

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.props.ChipStyle
import com.valkiria.uicomponents.props.TextStyle
import com.valkiria.uicomponents.props.toChipBorder
import com.valkiria.uicomponents.props.toChipColors
import com.valkiria.uicomponents.props.toTextStyle
import com.valkiria.uicomponents.theme.UiComponentsTheme
import com.valkiria.uicomponents.utlis.DefType.DRAWABLE
import com.valkiria.uicomponents.utlis.getResourceIdByName

@Composable
fun ChipComponent(
    uiModel: ChipUiModel
) {
    uiModel.text?.let { chipLabel ->
        val context: Context = LocalContext.current
        val iconResourceId = context.getResourceIdByName(uiModel.icon.orEmpty(), DRAWABLE)

        AssistChip(
            onClick = { },
            label = {
                Text(
                    text = chipLabel,
                    style = uiModel.textStyle.toTextStyle() // BACKEND: H5 is too small + not bold
                )
            },
            modifier = uiModel.modifier,
            leadingIcon = {
                if (iconResourceId != null) {
                    Icon(
                        painter = painterResource(id = iconResourceId),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }
            },
            shape = RoundedCornerShape(25.dp),
            colors = uiModel.style.toChipColors(),
            border = uiModel.style.toChipBorder()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChipComponentPreview() {
    UiComponentsTheme {
        /*
        {
            "identifier":"LOGIN_CODE_CHIP",
            "icon":"ic_ambulance",
            "text":"5421244",
            "text_style":"HEADLINE_5",
            "style":"PRIMARY",
            "type":"CHIP",
            "margins":{"top":20,"left":0,"right":0,"bottom":0}
        }
        */
        val chipUiModel = ChipUiModel(
            icon = "ic_ambulance",
            text = "5421244",
            textStyle = TextStyle.HEADLINE_5,
            style = ChipStyle.PRIMARY,
            modifier = Modifier
        )
        Column(
            modifier = Modifier.background(Color.DarkGray).padding(16.dp)
        ) {
            ChipComponent(
                uiModel = chipUiModel
            )
            ChipComponent(
                uiModel = chipUiModel.copy(
                    style = ChipStyle.SECONDARY
                )
            )
        }
    }
}
