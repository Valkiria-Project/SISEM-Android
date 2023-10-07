package com.valkiria.uicomponents.components.termsandconditions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.ireward.htmlcompose.HtmlText
import com.valkiria.uicomponents.components.body.TermsAndConditionsUiModel
import com.valkiria.uicomponents.model.mocks.getLoginTermsAndConditionsUiModel
import timber.log.Timber

@Composable
fun TermsAndConditionsComponent(
    uiModel: TermsAndConditionsUiModel,
    onAction: (link: String) -> Unit
) {
    Row(
        modifier = uiModel.modifier.fillMaxWidth(),
        horizontalArrangement = uiModel.arrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HtmlText(
            text =
            """<font color="#FFFFFF">Al ingresar aceptas nuestros</font> 
            |<a href="${uiModel.links.first()}">términos y condiciones</a> 
            |<font color="#FFFFFF">y nuestra</font> 
            |<a href="${uiModel.links[1]}">política de protección de datos personales.</a>
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
