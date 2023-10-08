@file:Suppress("TooManyFunctions")

package com.skgtecnologia.sisem.domain.model.banner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.domain.deviceauth.model.DeviceAuthIdentifier
import com.valkiria.uicomponents.components.button.ButtonUiModel
import com.valkiria.uicomponents.components.footerbody.FooterUiModel
import com.skgtecnologia.sisem.domain.preoperational.model.PreOperationalIdentifier
import com.skgtecnologia.sisem.domain.report.model.AddFindingIdentifier
import com.skgtecnologia.sisem.domain.report.model.AddReportIdentifier
import com.skgtecnologia.sisem.domain.report.model.ImagesConfirmationIdentifier
import com.valkiria.uicomponents.components.button.ButtonSize
import com.valkiria.uicomponents.components.button.ButtonStyle
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.button.OnClick

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

fun disassociateDeviceBanner(): BannerModel = BannerModel(
    icon = "ic_alert",
    title = "Continuar",
    description = "¿Desea continuar con las actividades en la aplicación?",
    footerModel = FooterUiModel(
        leftButton = ButtonUiModel(
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
        rightButton = ButtonUiModel(
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

fun findingCancellationBanner(): BannerModel = BannerModel(
    icon = "ic_alert",
    title = "¿Descartar hallazgo?",
    description = "Las modificaciones elaboradas no serán guardadas.",
    footerModel = FooterUiModel(
        leftButton = ButtonUiModel(
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
        rightButton = ButtonUiModel(
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

fun findingConfirmationBanner(): BannerModel = BannerModel(
    icon = "ic_alert",
    title = "Guardar hallazgo",
    description = "¿Desea guardar el hallazgo registrado?",
    footerModel = FooterUiModel(
        leftButton = ButtonUiModel(
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
        rightButton = ButtonUiModel(
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

fun findingSavedBanner(): BannerModel = BannerModel(
    icon = "ic_alert",
    iconColor = "#42A4FA",
    title = "Hallazgo guardado",
    description = "El hallazgo ha sido almacenado con éxito."
)

fun imagesLimitErrorBanner(imageLimit: Int): BannerModel = BannerModel(
    icon = "ic_alert",
    title = "Cantidad de fotos",
    description = """Se ha excedido el número de imágenes permitido por
        || el sistema $imageLimit""".trimMargin()
)

fun preOperationalConfirmationBanner(): BannerModel = BannerModel(
    icon = "ic_alert",
    title = "Guardar cambios",
    description = "\"Firmado electrónicamente - Unidad Funcional de APH unidad funcional, " +
        "en razón, de la obligación contractual \"7.22 elaborar y verificar el " +
        "diligenciamiento de la bitácora de estado de los elementos equipos biomédicos y de " +
        "radiocomunicaciones y demás que hacen parte de los vehículos de emergencia está\n" +
        "deberá ser diligenciada por cada una de las tripulaciones que entregan y reciben\n" +
        "turno en tiempo establecido",
    footerModel = FooterUiModel(
        leftButton = ButtonUiModel(
            identifier = PreOperationalIdentifier.PREOP_CANCEL_BUTTON.name,
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
        rightButton = ButtonUiModel(
            identifier = PreOperationalIdentifier.PREOP_SAVE_BUTTON.name,
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

fun preOperationalIncompleteFormBanner(): BannerModel = BannerModel(
    icon = "ic_alert",
    title = "Incompleto",
    description = "Para guardar el preoperacional es necesario que complete todos los campos."
)

fun reportCancellationBanner(): BannerModel = BannerModel(
    icon = "ic_alert",
    title = "Cancelar novedad",
    description = "¿Está seguro que desea cancelar el registro de la novedad?",
    footerModel = FooterUiModel(
        leftButton = ButtonUiModel(
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
        rightButton = ButtonUiModel(
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

fun reportConfirmationBanner(): BannerModel = BannerModel(
    icon = "ic_alert",
    title = "Guardar novedad",
    description = "¿Desea guardar la novedad registrada?",
    footerModel = FooterUiModel(
        leftButton = ButtonUiModel(
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
        rightButton = ButtonUiModel(
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

fun reportSentBanner(): BannerModel = BannerModel(
    icon = "ic_alert",
    iconColor = "#42A4FA",
    title = "Novedad guardada",
    description = "La novedad ha sido almacenada con éxito."
)
