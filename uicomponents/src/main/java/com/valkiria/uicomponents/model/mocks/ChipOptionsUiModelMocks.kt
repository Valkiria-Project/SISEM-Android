@file:Suppress("MagicNumber")

package com.valkiria.uicomponents.model.mocks

import androidx.compose.foundation.layout.Arrangement
import com.valkiria.uicomponents.model.props.TextModel
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.ui.body.ChipOptionsUiModel
import com.valkiria.uicomponents.model.ui.chip.ChipOptionUiModel
import kotlin.random.Random

fun getPreOperationalChipOptionsUiModel(): ChipOptionsUiModel {
    return ChipOptionsUiModel(
        identifier = Random(100).toString(),
        title = TextModel(
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
                name = "Direcciones",
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
        arrangement = Arrangement.Center
    )
}
