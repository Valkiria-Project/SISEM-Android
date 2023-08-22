package com.skgtecnologia.sisem.ui.imageselection

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.skgtecnologia.sisem.domain.model.header.HeaderModel
import com.skgtecnologia.sisem.domain.model.header.TextModel
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.props.TextStyle

@Suppress("LongMethod")
@Composable
fun ImageSelectionScreen(
    isTablet: Boolean,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (header, body, footer) = createRefs()

        HeaderSection(
            headerModel = getImageSelectionHeaderModel(),
            modifier = modifier.constrainAs(header) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
    }
}

private fun getImageSelectionHeaderModel() = HeaderModel(
    title = TextModel("Imágenes", TextStyle.HEADLINE_1),
    subtitle = TextModel(
        "Tome la foto o selecciónela para adjuntarla al hallazgo.",
        TextStyle.HEADLINE_5
    ),
    leftIcon = "ic_back", // FIXME: Add back icon
    modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
)
