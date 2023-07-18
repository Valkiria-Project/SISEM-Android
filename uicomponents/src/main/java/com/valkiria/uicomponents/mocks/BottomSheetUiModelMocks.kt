package com.valkiria.uicomponents.mocks

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.valkiria.uicomponents.R.drawable
import com.valkiria.uicomponents.components.bottomsheet.BottomSheetUiModel
import com.valkiria.uicomponents.props.TextStyle

@Composable
fun getLoginTermsBottomSheetUiModel(): BottomSheetUiModel {
    return BottomSheetUiModel(
        icon = painterResource(id = drawable.ic_message),
        title = "Términos y condiciones",
        titleTextStyle = TextStyle.HEADLINE_1,
        subtitle = "Conozca los términos y condiciones.",
        subtitleTextStyle = TextStyle.HEADLINE_6,
        text = """1. INTRODUCCIÓN\n\nLa Política de Seguridad de la Información del 
            |sitio web y términos de uso del Sitio Web de la Secretaría Distrital de 
            |Salud y el Fondo Financiero ...""".trimMargin(),
        textStyle = TextStyle.HEADLINE_5
    )
}

@Composable
fun getLoginPrivacyBottomSheetUiModel(): BottomSheetUiModel {
    return BottomSheetUiModel(
        icon = painterResource(id = drawable.ic_message),
        title = "Política de protección de datos",
        titleTextStyle = TextStyle.HEADLINE_1,
        subtitle = "Conozca la política de protección de datos.",
        subtitleTextStyle = TextStyle.HEADLINE_6,
        text = """1. INTRODUCCIÓN El presente documento responde a la necesidad de
            | dar cumplimiento a las disposiciones previstas en la Ley 1581 de 2012, 
            | que regula los deberes que asisten ...""".trimMargin(),
        textStyle = TextStyle.HEADLINE_5
    )
}
