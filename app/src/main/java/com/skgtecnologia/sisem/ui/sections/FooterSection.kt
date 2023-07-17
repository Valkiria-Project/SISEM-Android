package com.skgtecnologia.sisem.ui.sections

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.body.mapToUiModel
import com.skgtecnologia.sisem.domain.model.footer.FooterModel
import com.valkiria.uicomponents.components.button.ButtonComponent

@Composable
fun FooterSection(
    footerModel: FooterModel?,
    modifier: Modifier
) {
    footerModel?.let {
        Row {
            ButtonComponent(uiModel = footerModel.leftButton.mapToUiModel())

            footerModel.rightButton?.let {
                ButtonComponent(uiModel = it.mapToUiModel())
            }
        }
    }
}
