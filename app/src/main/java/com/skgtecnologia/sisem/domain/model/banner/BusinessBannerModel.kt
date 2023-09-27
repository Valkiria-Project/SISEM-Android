package com.skgtecnologia.sisem.domain.model.banner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.domain.deviceauth.model.DeviceAuthIdentifier
import com.skgtecnologia.sisem.domain.model.body.ButtonModel
import com.skgtecnologia.sisem.domain.model.footer.FooterModel
import com.skgtecnologia.sisem.domain.report.model.AddFindingIdentifier
import com.skgtecnologia.sisem.domain.report.model.AddReportIdentifier
import com.skgtecnologia.sisem.domain.report.model.ImagesConfirmationIdentifier
import com.valkiria.uicomponents.model.props.ButtonSize
import com.valkiria.uicomponents.model.props.ButtonStyle
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.ui.button.OnClick

fun cancelFindingBanner(): BannerModel = BannerModel(
    icon = "ic_alert",
    title = "¿Descartar hallazgo?",
    description = "Las modificaciones elaboradas no serán guardadas.",
    footerModel = FooterModel(
        leftButton = ButtonModel(
            identifier = AddFindingIdentifier.ADD_FINDING_CANCEL_BANNER.name,
            label = "CANCELAR",
            style = ButtonStyle.LOUD,
            textStyle = TextStyle.HEADLINE_5,
            onClick = OnClick.DISMISS,
            size = ButtonSize.DEFAULT,
            arrangement = Arrangement.Center,
            modifier = Modifier.padding(
                start = 0.dp,
                top = 20.dp,
                end = 0.dp,
                bottom = 0.dp
            )
        ),
        rightButton = ButtonModel(
            identifier = AddFindingIdentifier.ADD_FINDING_CONTINUE_BANNER.name,
            label = "CONTINUAR",
            style = ButtonStyle.LOUD,
            textStyle = TextStyle.HEADLINE_5,
            onClick = OnClick.DISMISS,
            size = ButtonSize.DEFAULT,
            arrangement = Arrangement.Center,
            modifier = Modifier.padding(
                start = 0.dp,
                top = 20.dp,
                end = 0.dp,
                bottom = 0.dp
            )
        )
    )
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
                start = 0.dp,
                top = 20.dp,
                end = 0.dp,
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
                start = 0.dp,
                top = 20.dp,
                end = 0.dp,
                bottom = 0.dp
            )
        )
    )
)

fun changePasswordEmptyFieldsBanner(): BannerModel = BannerModel(
    icon = "ic_alert",
    title = "Incompleto",
    description = "Para guardar el cambio de contraseña es necesario que complete todos los campos."
)

fun changePasswordNoMatchBanner(): BannerModel = BannerModel(
    icon = "ic_alert",
    title = "Nueva contraseña",
    description = "Nueva contraseña y confirmar nueva contraseña no coinciden."
)

fun changePasswordSuccessBanner(): BannerModel = BannerModel(
    icon = "ic_alert",
    iconColor = "#42A4FA",
    title = "Nueva contraseña",
    description = "La contraseña se ha cambiado con éxito."
)

fun confirmFindingBanner(): BannerModel = BannerModel(
    icon = "ic_alert",
    title = "Guardar cambios",
    description = "Firmado electrónicamente - Unidad Funcional de APH <unidad funcional>, " +
        "en razón, de la obligación contractual \"7.22 elaborar y verificar el " +
        "diligenciamiento de la bitácora de estado de los elementos equipos biomédicos y de " +
        "radiocomunicaciones y demás que hacen parte de los vehículos de emergencia está\n" +
        "deberá ser diligenciada por cada una de las tripulaciones que entregan y reciben\n" +
        "turno en tiempo establecido",
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
                start = 0.dp,
                top = 20.dp,
                end = 0.dp,
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
                start = 0.dp,
                top = 20.dp,
                end = 0.dp,
                bottom = 0.dp
            )
        )
    )
)

fun confirmReportBanner(): BannerModel = BannerModel(
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
                start = 0.dp,
                top = 20.dp,
                end = 0.dp,
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
                start = 0.dp,
                top = 20.dp,
                end = 0.dp,
                bottom = 0.dp
            )
        )
    )
)

fun disassociateDeviceBanner(): BannerModel = BannerModel(
    icon = "ic_alert",
    title = "Continuar",
    description = "¿Desea continuar con las actividades en la aplicación?",
    footerModel = FooterModel(
        leftButton = ButtonModel(
            identifier = DeviceAuthIdentifier.DEVICE_AUTH_CANCEL_BANNER.name,
            label = "CANCELAR",
            style = ButtonStyle.LOUD,
            textStyle = TextStyle.HEADLINE_5,
            onClick = OnClick.DISMISS,
            size = ButtonSize.DEFAULT,
            arrangement = Arrangement.Center,
            modifier = Modifier.padding(
                start = 0.dp,
                top = 20.dp,
                end = 0.dp,
                bottom = 0.dp
            )
        ),
        rightButton = ButtonModel(
            identifier = DeviceAuthIdentifier.DEVICE_AUTH_CONTINUE_BANNER.name,
            label = "CONTINUAR",
            style = ButtonStyle.LOUD,
            textStyle = TextStyle.HEADLINE_5,
            onClick = OnClick.DISMISS,
            size = ButtonSize.DEFAULT,
            arrangement = Arrangement.Center,
            modifier = Modifier.padding(
                start = 0.dp,
                top = 20.dp,
                end = 0.dp,
                bottom = 0.dp
            )
        )
    )
)
