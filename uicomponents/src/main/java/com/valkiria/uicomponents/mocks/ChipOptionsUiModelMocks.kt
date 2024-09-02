package com.valkiria.uicomponents.mocks

import androidx.compose.foundation.layout.Arrangement
import com.valkiria.uicomponents.components.chip.ChipOptionUiModel
import com.valkiria.uicomponents.components.chip.ChipOptionsUiModel
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel

fun getPreOperationalChipOptionsUiModel(): ChipOptionsUiModel {
    return ChipOptionsUiModel(
        identifier = "PRE_OP_CHIP_OPTIONS",
        title = TextUiModel(
            "Seleccione las herramientas que hacen falta",
            TextStyle.BODY_1,
        ),
        items = listOf(
            ChipOptionUiModel(
                id = "1",
                name = "Cruceta",
                selected = false
            ),
            ChipOptionUiModel(
                id = "2",
                name = "Directions",
                selected = false
            ),
            ChipOptionUiModel(
                id = "3",
                name = "Alicate pala",
                selected = false
            ),
            ChipOptionUiModel(
                id = "4",
                name = "Bisturí",
                selected = false
            ),
            ChipOptionUiModel(
                id = "5",
                name = "Caja de fusibles",
                selected = false
            ),
            ChipOptionUiModel(
                id = "6",
                name = "Lampara busca",
                selected = false
            ),
            ChipOptionUiModel(
                id = "7",
                name = "Chaleco reflectivo",
                selected = false
            ),
            ChipOptionUiModel(
                id = "8",
                name = "Gato hidráulico",
                selected = false
            ),
            ChipOptionUiModel(
                id = "9",
                name = "Juego de destornilladores",
                selected = false
            )
        ),
        required = false,
        visibility = true,
        arrangement = Arrangement.Center
    )
}
