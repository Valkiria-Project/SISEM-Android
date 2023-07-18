package com.skgtecnologia.sisem.ui.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.domain.model.body.mapToUiModel
import com.skgtecnologia.sisem.domain.model.footer.FooterModel
import com.valkiria.uicomponents.action.FooterUiAction.Button
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.button.ButtonView

@Composable
fun FooterSection(
    footerModel: FooterModel,
    isTablet: Boolean = false,
    modifier: Modifier,
    onAction: (actionInput: UiAction) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally)
    ) {
        ButtonView(
            uiModel = footerModel.leftButton.mapToUiModel(),
            isTablet = isTablet
        ) {
            onAction(Button(footerModel.leftButton.identifier))
        }

        footerModel.rightButton?.let {
            ButtonView(
                uiModel = it.mapToUiModel(),
                isTablet = isTablet
            ) {
                onAction(Button(footerModel.rightButton.identifier))
            }
        }
    }
}
