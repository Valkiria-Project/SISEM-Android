package com.skgtecnologia.sisem.commons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.components.chip.ChipOptionUiModel
import com.valkiria.uicomponents.components.chip.ChipOptionsUiModel
import com.valkiria.uicomponents.components.finding.FindingUiModel
import com.valkiria.uicomponents.components.inventorycheck.InventoryCheckItemUiModel
import com.valkiria.uicomponents.components.inventorycheck.InventoryCheckUiModel
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.richlabel.RichLabelUiModel
import com.valkiria.uicomponents.components.segmentedswitch.SegmentedSwitchUiModel
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel

const val ANDROID_ID = "123"
const val SERVER_ERROR_TITLE = "Error en servidor"
const val USERNAME = "username"
const val PASSWORD = "password"
const val SERIAL = "serial"
const val CODE = "123"
const val EMAIL = "email"
const val PATIENT_ID = "patientId"
const val INCIDENT_CODE = "incidentCode"

val emptyScreenModel = ScreenModel(body = emptyList())

val chipOptionsUiModelMock = ChipOptionsUiModel(
    identifier = "identifier",
    items = listOf(
        ChipOptionUiModel(
            id = "identifier",
            name = "text",
            selected = false
        )
    ),
    arrangement = Arrangement.Start,
    modifier = Modifier
)

val findingUiModelMock = FindingUiModel(
    identifier = "identifier",
    segmentedValueUiModel = null,
    segmentedSwitchUiModel = SegmentedSwitchUiModel(
        identifier = "identifier",
        text = "text",
        textStyle = TextStyle.BODY_1,
        selected = false,
        options = listOf(),
        arrangement = Arrangement.Start,
        modifier = Modifier
    ),
    readOnly = false,
    findingDetail = null,
    arrangement = Arrangement.Start,
    modifier = Modifier
)

val inventoryCheckUiModelMock = InventoryCheckUiModel(
    identifier = "identifier",
    registered = TextUiModel(
        text = "text",
        textStyle = TextStyle.BODY_1
    ),
    received = TextUiModel(
        text = "text",
        textStyle = TextStyle.BODY_1
    ),
    items = listOf(
        InventoryCheckItemUiModel(
            name = RichLabelUiModel(
                identifier = "identifier",
                text = "text",
                textStyle = TextStyle.BODY_1,
                arrangement = Arrangement.Start,
                modifier = Modifier
            ),
            registeredValueText = "text",
            registeredValueTextStyle = TextStyle.BODY_1,
            receivedValueText = "text",
            receivedValueTextStyle = TextStyle.BODY_1,
        )
    ),
    validations = listOf(),
    arrangement = Arrangement.Start,
    modifier = Modifier
)

val textFieldUiModelMock = TextFieldUiModel(
    identifier = "identifier",
    keyboardOptions = KeyboardOptions.Default,
    textStyle = TextStyle.BODY_1,
    charLimit = 0,
    validations = listOf(),
    arrangement = Arrangement.Start,
    modifier = Modifier
)

val richLabelUiModelMock = RichLabelUiModel(
    identifier = "id",
    text = "text",
    textStyle = TextStyle.BODY_1,
    arrangement = Arrangement.Start,
    modifier = Modifier
)
