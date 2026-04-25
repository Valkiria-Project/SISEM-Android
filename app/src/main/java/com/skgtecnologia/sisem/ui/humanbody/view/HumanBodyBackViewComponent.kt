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
import com.skgtecnologia.sisem.ui.humanbody.area.BackArea
import com.valkiria.uicomponents.components.humanbody.HumanBodyUi

@Composable
fun HumanBodyBackViewComponent(
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
            painter = painterResource(id = R.drawable.img_back_human_body),
            contentDescription = null
        )

        wounds?.forEach {
            Image(
                modifier = Modifier
                    .align(Alignment.Center),
                painter = painterResource(id = BackArea.fromName(it.area).image),
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
fun HumanBodyBackViewComponentPreview() {
    HumanBodyBackViewComponent(
        wounds = listOf(
            HumanBodyUi(
                type = "BACK",
                area = "HEAD",
                areaName = "Cabeza",
                wounds = listOf("Herida 1", "Herida 2")
            ),
            HumanBodyUi(
                type = "BACK",
                area = "BACK",
                areaName = "Espalda",
                wounds = listOf("Herida 1", "Herida 2")
            )
        )
    ) {}
}
