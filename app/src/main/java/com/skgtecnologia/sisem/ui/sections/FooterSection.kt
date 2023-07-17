package com.skgtecnologia.sisem.ui.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.domain.model.body.ButtonModel
import com.skgtecnologia.sisem.domain.model.footer.FooterModel
import com.valkiria.uicomponents.props.ButtonSize
import com.valkiria.uicomponents.props.mapToColors
import com.valkiria.uicomponents.props.mapToTextColor
import com.valkiria.uicomponents.props.toTextStyle

@Composable
fun FooterSection(
    footerModel: FooterModel,
    isTablet: Boolean = false,
    modifier: Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally)
    ) {
        FooterButton(buttonModel = footerModel.leftButton, isTablet = isTablet)

        footerModel.rightButton?.let {
            FooterButton(buttonModel = it, isTablet = isTablet)
        }
    }
}

@Composable
private fun FooterButton(
    buttonModel: ButtonModel,
    isTablet: Boolean
) {
    Button(
        onClick = { },
        colors = buttonModel.style.mapToColors(),
        modifier = if (buttonModel.size == ButtonSize.FULL_WIDTH) {
            Modifier.fillMaxWidth()
        } else {
            Modifier
        }
    ) {
        Text(
            text = buttonModel.label,
            color = buttonModel.style.mapToTextColor(),
            style = buttonModel.textStyle.toTextStyle()
        )
    }
}
