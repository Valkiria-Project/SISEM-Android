package com.valkiria.uicomponents.model.mocks

import androidx.compose.foundation.layout.Arrangement
import com.valkiria.uicomponents.components.detailedinfolist.DetailedInfoUiModel
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.components.detailedinfolist.DetailedInfoListUiModel

fun getPreOperationalDetailedInfoListUiModel(): DetailedInfoListUiModel {
    return DetailedInfoListUiModel(
        identifier = "PRE_OP_REGISTRATION_DETAILS",
        details = listOf(
            DetailedInfoUiModel(
                label = "Registro",
                icon = "ic_calendar",
                text = "20/03/2023"
            )
        ),
        labelTextStyle = TextStyle.BUTTON_1,
        textTextStyle = TextStyle.HEADLINE_4,
        arrangement = Arrangement.Center
    )
}
