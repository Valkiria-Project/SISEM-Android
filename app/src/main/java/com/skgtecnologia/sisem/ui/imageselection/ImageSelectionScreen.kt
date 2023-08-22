package com.skgtecnologia.sisem.ui.imageselection

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.domain.model.header.HeaderModel
import com.skgtecnologia.sisem.domain.model.header.TextModel
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.props.TabletWidth
import com.valkiria.uicomponents.props.TextStyle

@Suppress("LongMethod")
@Composable
fun ImageSelectionScreen(
    isTablet: Boolean,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = if (isTablet) {
            modifier.width(TabletWidth)
        } else {
            modifier.fillMaxWidth()
        },
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

@Composable
private fun getImageSelectionHeaderModel() = HeaderModel(
    title = TextModel(
        stringResource(id = R.string.image_selection_title),
        TextStyle.HEADLINE_1
    ),
    subtitle = TextModel(
        stringResource(R.string.image_selection_subtitle),
        TextStyle.HEADLINE_5
    ),
    leftIcon = stringResource(R.string.image_selection_left_icon),
    modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
)
