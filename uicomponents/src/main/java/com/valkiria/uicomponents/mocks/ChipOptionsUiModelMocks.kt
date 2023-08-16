@file:Suppress("MagicNumber")

package com.valkiria.uicomponents.mocks

import com.valkiria.uicomponents.components.chipoptions.ChipOptionsUiModel
import com.valkiria.uicomponents.props.TextStyle
import kotlin.random.Random

fun getPreOperationalChipOptionsUiModel(): ChipOptionsUiModel {
    return ChipOptionsUiModel(
        identifier = Random(100).toString(),
        title = "Seleccione las herramientas que hacen falta",
        textStyle = TextStyle.BODY_1,
        items = listOf(
            "Cruceta",
            "Direcciones",
            "Alicate pala",
            "Bisturí",
            "Caja de fusibles",
            "Lampara busca",
            "Chaleco reflectivo",
            "Gato hidráulico",
            "Juego de destornilladores"
        )
    )
}
