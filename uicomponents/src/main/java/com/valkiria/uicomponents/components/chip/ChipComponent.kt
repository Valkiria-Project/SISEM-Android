package com.valkiria.uicomponents.components.chip

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.props.ChipStyle
import com.valkiria.uicomponents.props.TextStyle
import com.valkiria.uicomponents.theme.lobsterTwoFontFamily
import com.valkiria.uicomponents.theme.montserratFontFamily

@Composable
fun ChipComponent(
    uiModel: ChipUiModel,
    modifier: Modifier = Modifier,
    fontFamily: FontFamily = FontFamily.Default
) {
    uiModel.text?.let { chipLabel ->
        AssistChip(
            onClick = { },
            label = { // FIXME: Create function to extract the TextStyle from the input
                Text(
                    text = chipLabel,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamily
                )
            },
            modifier = uiModel.modifier,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_ambulancia), // FIXME: Create function to extract the ic from the input
                    contentDescription = "recurso test",
                    modifier = modifier.size(40.dp)
                )
            },
            shape = RoundedCornerShape(25.dp),
            colors = AssistChipDefaults.assistChipColors( // FIXME: Create function to extract the style
                containerColor = MaterialTheme.colorScheme.primary,
                labelColor = Color.Black,
                leadingIconContentColor = Color.Black
            ),
            border = AssistChipDefaults.assistChipBorder( // FIXME: Create function to extract the style
                borderColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Preview
@Composable
fun ChipComponentPreview() {
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
    Column {
        ChipComponent(
            uiModel = chipUiModel,
            fontFamily = montserratFontFamily
        )
        ChipComponent(
            uiModel = chipUiModel,
            fontFamily = lobsterTwoFontFamily
        )
    }
}
