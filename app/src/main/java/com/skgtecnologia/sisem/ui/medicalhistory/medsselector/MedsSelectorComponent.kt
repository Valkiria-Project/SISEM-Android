package com.skgtecnologia.sisem.ui.medicalhistory.medsselector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.bricks.chip.ChipSectionUiModel
import com.valkiria.uicomponents.components.button.ButtonComponent
import com.valkiria.uicomponents.components.button.ButtonSize
import com.valkiria.uicomponents.components.button.ButtonStyle
import com.valkiria.uicomponents.components.button.ButtonUiModel
import com.valkiria.uicomponents.components.button.OnClick
import com.valkiria.uicomponents.components.card.InfoCardComponent
import com.valkiria.uicomponents.components.card.InfoCardUiModel
import com.valkiria.uicomponents.components.card.PillUiModel
import com.valkiria.uicomponents.components.label.ListTextUiModel
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.medsselector.MedsSelectorUiModel

@Composable
fun MedsSelectorComponent(
    uiModel: MedsSelectorUiModel,
    onAction: (id: String) -> Unit
) {
    Row(
        modifier = uiModel.modifier.fillMaxWidth(),
        horizontalArrangement = uiModel.arrangement
    ) {
        Column {
            uiModel.medicines.forEach { medicine ->
                InfoCardComponent(uiModel = medicine, onAction = {})
            }

            ButtonComponent(
                uiModel = uiModel.button,
                onAction = { onAction(uiModel.identifier) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MedsSelectorComponentPreview() {
    MedsSelectorComponent(
        uiModel = MedsSelectorUiModel(
            identifier = "meds_selector",
            button = ButtonUiModel(
                identifier = "meds_selector_button",
                label = "Guardar",
                leftIcon = "ic_add",
                style = ButtonStyle.LOUD,
                textStyle = TextStyle.HEADLINE_4,
                onClick = OnClick.SAVE,
                size = ButtonSize.FULL_WIDTH,
                arrangement = Arrangement.Center,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp)
            ),
            medicines = listOf(
                InfoCardUiModel(
                    identifier = "meds_selector_card_1",
                    icon = "ic_pill",
                    title = TextUiModel(
                        text = "Penicilina",
                        textStyle = TextStyle.HEADLINE_4
                    ),
                    pill = PillUiModel(
                        title = TextUiModel(
                            text = "20/06/2023 - 13:00",
                            textStyle = TextStyle.BUTTON_1
                        ),
                        color = "#3E4146"
                    ),
                    date = null,
                    chipSection = ChipSectionUiModel(
                        title = TextUiModel(
                            text = "Especificaciones",
                            textStyle = TextStyle.HEADLINE_7
                        ),
                        listText = ListTextUiModel(
                            texts = listOf(
                                "Dosis aplicada 10mg",
                                "Código 203055",
                                "Cantidad utilizada 20mg",
                                "Via de administración Intramuscular"
                            ),
                            textStyle = TextStyle.BUTTON_1
                        )
                    ),
                    reportsDetail = null,
                    arrangement = Arrangement.Center,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                )
            ),
            section = null,
            arrangement = Arrangement.Center,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp)
        ),
        onAction = {}
    )
}
