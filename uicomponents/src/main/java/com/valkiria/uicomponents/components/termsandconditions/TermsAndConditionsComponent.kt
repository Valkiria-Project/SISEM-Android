package com.valkiria.uicomponents.components.termsandconditions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.ireward.htmlcompose.HtmlText
import com.valkiria.uicomponents.mocks.getLoginTermsAndConditionsUiModel
import com.valkiria.uicomponents.props.TabletWidth
import com.valkiria.uicomponents.theme.UiComponentsTheme
import timber.log.Timber

@Composable
fun TermsAndConditionsComponent(
    uiModel: TermsAndConditionsUiModel,
    isTablet: Boolean = false,
    onAction: (link: String) -> Unit
) {
    Row(
        modifier = if (isTablet) {
            uiModel.modifier.width(TabletWidth)
        } else {
            uiModel.modifier.fillMaxWidth()
        },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HtmlText(
            text =
            """<font color="#FFFFFF">Al ingresar aceptas nuestros</font> 
            |<a href="${uiModel.termsAndConditionsLink}">términos y condiciones</a> 
            |<font color="#FFFFFF">y nuestra</font> 
            |<a href="${uiModel.privacyPolicyLink}">política de protección de datos personales.</a>
            |</font>""".trimMargin(),
            style = MaterialTheme.typography.labelMedium,
            linkClicked = { link ->
                Timber.d("$link clicked")
                onAction(link)
            },
            URLSpanStyle = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.None
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TermsAndConditionsComponentPreview() {
    UiComponentsTheme {
        Column(
            modifier = Modifier.background(Color.DarkGray)
        ) {
            TermsAndConditionsComponent(
                uiModel = getLoginTermsAndConditionsUiModel()
            ) { link ->
                Timber.d("Handle $link clicked")
            }
        }
    }
}
