@file:Suppress("MagicNumber")

package com.valkiria.uicomponents.mocks

import com.valkiria.uicomponents.components.chipoptions.ChipOptionUiModel
import com.valkiria.uicomponents.components.chipoptions.ChipOptionsUiModel
import com.valkiria.uicomponents.props.TextStyle
import kotlin.random.Random

fun getPreOperationalChipOptionsUiModel(): ChipOptionsUiModel {
    return ChipOptionsUiModel(
        identifier = Random(100).toString(),
        title = "Seleccione las herramientas que hacen falta",
        textStyle = TextStyle.BODY_1,
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
        )
    )
}