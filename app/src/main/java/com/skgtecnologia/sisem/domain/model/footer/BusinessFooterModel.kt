package com.skgtecnologia.sisem.domain.model.footer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.ui.body.ButtonModel
import com.skgtecnologia.sisem.domain.report.model.AddFindingIdentifier
import com.valkiria.uicomponents.model.props.ButtonSize
import com.valkiria.uicomponents.model.props.ButtonStyle
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.ui.button.OnClick

fun findingsFooter(
    leftButtonText: String,
    rightButtonText: String,
): FooterModel = FooterModel(
    leftButton = ButtonModel(
        identifier = AddFindingIdentifier.ADD_FINDING_CANCEL_BUTTON.name,
        label = leftButtonText,
        style = ButtonStyle.LOUD,
        textStyle = TextStyle.HEADLINE_4,
        onClick = OnClick.DISMISS,
        size = ButtonSize.DEFAULT,
        arrangement = Arrangement.Center,
        modifier = Modifier
    ),
    rightButton = ButtonModel(
        identifier = AddFindingIdentifier.ADD_FINDING_SAVE_BUTTON.name,
        label = rightButtonText,
        style = ButtonStyle.LOUD,
        textStyle = TextStyle.HEADLINE_4,
        onClick = OnClick.DISMISS,
        size = ButtonSize.DEFAULT,
        arrangement = Arrangement.Center,
        modifier = Modifier
    )
)
