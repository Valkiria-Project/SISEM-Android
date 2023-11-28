package com.skgtecnologia.sisem.ui.humanbody.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.ui.humanbody.SwitchBodyType
import com.skgtecnologia.sisem.ui.humanbody.area.FrontArea
import com.valkiria.uicomponents.components.humanbody.HumanBodyUi

@Composable
fun HumanBodyFrontViewComponent(
    modifier: Modifier = Modifier,
    wounds: List<HumanBodyUi>?,
    onAction: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.Center),
            painter = painterResource(id = R.drawable.img_front_human_body),
            contentDescription = null
        )

        wounds?.forEach {
            Image(
                modifier = Modifier
                    .align(Alignment.Center),
                painter = painterResource(id = FrontArea.fromName(it.area).image),
                contentDescription = null
            )
        }

        SwitchBodyType(modifier = Modifier.align(Alignment.BottomEnd)) {
            onAction()
        }
    }
}

@Composable
@Preview
fun HumanBodyFrontViewComponentPreview() {
    HumanBodyFrontViewComponent(
        wounds = listOf(
            HumanBodyUi(
                area = "HEAD",
                type = "FRONT",
                wounds = listOf("Herida 1", "Herida 2")
            ),
            HumanBodyUi(
                area = "CHEST",
                type = "FRONT",
                wounds = listOf("Herida 1", "Herida 2")
            )
        )
    ) {}
}
