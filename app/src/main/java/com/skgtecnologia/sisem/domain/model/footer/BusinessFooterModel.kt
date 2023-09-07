package com.skgtecnologia.sisem.domain.model.footer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.domain.model.body.ButtonModel
import com.skgtecnologia.sisem.domain.model.body.FooterBodyModel
import com.skgtecnologia.sisem.domain.preoperational.model.PreOperationalIdentifier
import com.valkiria.uicomponents.model.props.ButtonSize
import com.valkiria.uicomponents.model.props.ButtonStyle
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.ui.button.OnClick

fun preOperationalAuxiliaryFooterModel(): FooterBodyModel = FooterBodyModel(
    leftButton = ButtonModel(
        identifier = PreOperationalIdentifier.ASSISTANT_PREOP_SAVE_BUTTON.name,
        label = "GUARDAR",
        style = ButtonStyle.LOUD,
        textStyle = TextStyle.HEADLINE_4,
        onClick = OnClick.DISMISS,
        size = ButtonSize.FULL_WIDTH,
        arrangement = Arrangement.Center,
        modifier = Modifier.padding(
            start = 0.dp,
            top = 20.dp,
            end = 0.dp,
            bottom = 0.dp
        )
    )
)

fun preOperationalDriverFooterModel(): FooterBodyModel = FooterBodyModel(
    leftButton = ButtonModel(
        identifier = PreOperationalIdentifier.DRIVER_PREOP_SAVE_BUTTON.name,
        label = "GUARDAR",
        style = ButtonStyle.LOUD,
        textStyle = TextStyle.HEADLINE_4,
        onClick = OnClick.DISMISS,
        size = ButtonSize.FULL_WIDTH,
        arrangement = Arrangement.Center,
        modifier = Modifier.padding(
            start = 0.dp,
            top = 20.dp,
            end = 0.dp,
            bottom = 0.dp
        )
    )
)

fun preOperationalMedicFooterModel(): FooterBodyModel = FooterBodyModel(
    leftButton = ButtonModel(
        identifier = PreOperationalIdentifier.DOCTOR_PREOP_SAVE_BUTTON.name,
        label = "GUARDAR",
        style = ButtonStyle.LOUD,
        textStyle = TextStyle.HEADLINE_4,
        onClick = OnClick.DISMISS,
        size = ButtonSize.FULL_WIDTH,
        arrangement = Arrangement.Center,
        modifier = Modifier.padding(
            start = 0.dp,
            top = 20.dp,
            end = 0.dp,
            bottom = 0.dp
        )
    )
)
