package com.skgtecnologia.sisem.ui.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.action.FooterUiAction.FooterButton
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.button.ButtonView
import com.valkiria.uicomponents.components.footer.FooterUiModel

@Composable
fun FooterSection(
    footerModel: FooterUiModel,
    modifier: Modifier = Modifier,
    onAction: (actionInput: UiAction) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally)
    ) {
        ButtonView(uiModel = footerModel.leftButton) {
            onAction(FooterButton(footerModel.leftButton.identifier))
        }

        footerModel.rightButton?.let {
            ButtonView(uiModel = it) {
                onAction(FooterButton(it.identifier))
            }
        }
    }
}
