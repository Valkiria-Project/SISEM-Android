package com.skgtecnologia.sisem.domain.model.banner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.domain.report.model.ImagesConfirmationIdentifier
import com.skgtecnologia.sisem.domain.model.body.ButtonModel
import com.skgtecnologia.sisem.domain.model.footer.FooterModel
import com.skgtecnologia.sisem.domain.report.model.AddReportIdentifier
import com.valkiria.uicomponents.model.props.ButtonSize
import com.valkiria.uicomponents.model.props.ButtonStyle
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.ui.button.OnClick

// FIXME: Handle this in the use Cases
fun changePasswordEmptyFields(): BannerModel = BannerModel(
    icon = "ic_alert",
    title = "Incompleto",
    description = "Para guardar el cambio de contraseña es necesario que complete todos los campos."
)

// FIXME: Handle this in the use Cases
fun changePasswordNoMatch(): BannerModel = BannerModel(
    icon = "ic_alert",
    title = "Nueva contraseña",
    description = "Nueva contraseña y confirmar nueva contraseña no coinciden."
)

// FIXME: Delete this with refactor de banner commponent
fun changePasswordSuccess(): BannerModel = BannerModel(
    icon = "ic_alert",
    iconColor = "#42A4FA",
    title = "Nueva contraseña",
    description = "La contraseña se ha cambiado con éxito."
)

fun cancelReportBanner(): BannerModel = BannerModel(
    icon = "ic_alert",
    title = "Cancelar novedad",
    description = "¿Está seguro que desea cancelar el registro de la novedad?",
    footerModel = FooterModel(
        leftButton = ButtonModel(
            identifier = AddReportIdentifier.ADD_REPORT_CANCEL_BANNER.name,
            label = "CANCELAR",
            style = ButtonStyle.LOUD,
            textStyle = TextStyle.HEADLINE_5,
            onClick = OnClick.DISMISS,
            size = ButtonSize.DEFAULT,
            arrangement = Arrangement.Center,
            modifier = Modifier.padding(
                start = 20.dp,
                top = 20.dp,
                end = 20.dp,
                bottom = 0.dp
            )
        ),
        rightButton = ButtonModel(
            identifier = AddReportIdentifier.ADD_REPORT_CONTINUE_BANNER.name,
            label = "CONTINUAR",
            style = ButtonStyle.LOUD,
            textStyle = TextStyle.HEADLINE_5,
            onClick = OnClick.DISMISS,
            size = ButtonSize.DEFAULT,
            arrangement = Arrangement.Center,
            modifier = Modifier.padding(
                start = 20.dp,
                top = 20.dp,
                end = 20.dp,
                bottom = 0.dp
            )
        )
    )
)

fun confirmSendReportBanner(): BannerModel = BannerModel(
    icon = "ic_alert",
    title = "Guardar novedad",
    description = "¿Desea guardar la novedad registrada?",
    footerModel = FooterModel(
        leftButton = ButtonModel(
            identifier = ImagesConfirmationIdentifier.IMAGES_CONFIRMATION_CANCEL_BANNER.name,
            label = "CANCELAR",
            style = ButtonStyle.LOUD,
            textStyle = TextStyle.HEADLINE_5,
            onClick = OnClick.DISMISS,
            size = ButtonSize.DEFAULT,
            arrangement = Arrangement.Center,
            modifier = Modifier.padding(
                start = 20.dp,
                top = 20.dp,
                end = 20.dp,
                bottom = 0.dp
            )
        ),
        rightButton = ButtonModel(
            identifier = ImagesConfirmationIdentifier.IMAGES_CONFIRMATION_SEND_BANNER.name,
            label = "GUARDAR",
            style = ButtonStyle.LOUD,
            textStyle = TextStyle.HEADLINE_5,
            onClick = OnClick.DISMISS,
            size = ButtonSize.DEFAULT,
            arrangement = Arrangement.Center,
            modifier = Modifier.padding(
                start = 20.dp,
                top = 20.dp,
                end = 20.dp,
                bottom = 0.dp
            )
        )
    )
)
