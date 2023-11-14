package com.skgtecnologia.sisem.commons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.login.model.LoginIdentifier
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.components.button.ButtonSize
import com.valkiria.uicomponents.components.button.ButtonStyle
import com.valkiria.uicomponents.components.button.ButtonUiModel
import com.valkiria.uicomponents.components.button.ImageButtonOptionUiModel
import com.valkiria.uicomponents.components.button.ImageButtonSectionUiModel
import com.valkiria.uicomponents.components.button.ImageButtonUiModel
import com.valkiria.uicomponents.components.button.OnClick
import com.valkiria.uicomponents.components.card.InfoCardUiModel
import com.valkiria.uicomponents.components.card.PillUiModel
import com.valkiria.uicomponents.components.chip.ChipOptionUiModel
import com.valkiria.uicomponents.components.chip.ChipOptionsUiModel
import com.valkiria.uicomponents.components.chip.ChipSelectionItemUiModel
import com.valkiria.uicomponents.components.chip.ChipSelectionUiModel
import com.valkiria.uicomponents.components.dropdown.DropDownItemUiModel
import com.valkiria.uicomponents.components.dropdown.DropDownUiModel
import com.valkiria.uicomponents.components.finding.FindingUiModel
import com.valkiria.uicomponents.components.header.HeaderUiModel
import com.valkiria.uicomponents.components.inventorycheck.InventoryCheckItemUiModel
import com.valkiria.uicomponents.components.inventorycheck.InventoryCheckUiModel
import com.valkiria.uicomponents.components.label.LabelUiModel
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.media.MediaActionsUiModel
import com.valkiria.uicomponents.components.medsselector.MedsSelectorUiModel
import com.valkiria.uicomponents.components.richlabel.RichLabelUiModel
import com.valkiria.uicomponents.components.segmentedswitch.OptionUiModel
import com.valkiria.uicomponents.components.segmentedswitch.SegmentedSwitchUiModel
import com.valkiria.uicomponents.components.signature.SignatureUiModel
import com.valkiria.uicomponents.components.slider.SliderUiModel
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
const val TOPIC = "topic"
const val DESCRIPTION = "description"

val uiAction = FooterUiAction.FooterButton(LoginIdentifier.LOGIN_RE_AUTH_BANNER.name)
val emptyScreenModel = ScreenModel(body = emptyList())

val chipOptionsUiModelMock = ChipOptionsUiModel(
    identifier = "identifier",
    items = listOf(
        ChipOptionUiModel(
            id = "identifier",
            name = "text",
            selected = true
        )
    ),
    arrangement = Arrangement.Start,
    modifier = Modifier
)

val chipSelectionUiModelMock = ChipSelectionUiModel(
    identifier = "identifier",
    title = TextUiModel(
        text = "text",
        textStyle = TextStyle.BODY_1
    ),
    items = listOf(
        ChipSelectionItemUiModel(
            id = "identifier",
            name = "text"
        )
    ),
    selected = "identifier",
    arrangement = Arrangement.Start,
    modifier = Modifier
)

val dropDownUiModelMock = DropDownUiModel(
    identifier = "identifier",
    label = "text",
    items = listOf(
        DropDownItemUiModel(
            id = "identifier",
            name = "text"
        )
    ),
    header = HeaderUiModel(
        identifier = "identifier",
        title = TextUiModel(
            text = "text",
            textStyle = TextStyle.BODY_1
        ),
        arrangement = Arrangement.Start,
        modifier = Modifier
    ),
    selected = "identifier",
    section = null,
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

val imageButtonSectionUiModelMock = ImageButtonSectionUiModel(
    identifier = "identifier",
    options = listOf(
        ImageButtonOptionUiModel(
            identifier = "identifier",
            title = TextUiModel(
                text = "text",
                textStyle = TextStyle.BODY_1
            ),
            options = listOf(
                ImageButtonUiModel(
                    identifier = "identifier",
                    title = TextUiModel(
                        text = "text",
                        textStyle = TextStyle.BODY_1
                    ),
                    image = "",
                    selected = false,
                    arrangement = Arrangement.Start,
                    modifier = Modifier
                )
            ),
            modifier = Modifier
        )
    ),
    arrangement = Arrangement.Start,
    modifier = Modifier
)

val infoCardUiModelMock = InfoCardUiModel(
    identifier = "identifier",
    icon = "icon",
    title = TextUiModel(
        text = "text",
        textStyle = TextStyle.BODY_1
    ),
    pill = PillUiModel(
        title = TextUiModel(
            text = "text",
            textStyle = TextStyle.BODY_1
        ),
        color = "color"
    ),
    date = TextUiModel(
        text = "text",
        textStyle = TextStyle.BODY_1
    ),
    chipSection = null,
    reportsDetail = null,
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

val labelUiModelMock = LabelUiModel(
    identifier = "identifier",
    text = "text",
    textStyle = TextStyle.BODY_1,
    arrangement = Arrangement.Start,
    modifier = Modifier
)

val mediaActionsUiModelMock = MediaActionsUiModel(
    identifier = "identifier",
    withinForm = false,
    modifier = Modifier
)

val medsSelectorUiModelMock = MedsSelectorUiModel(
    identifier = "identifier",
    button = ButtonUiModel(
        identifier = "identifier",
        label = "text",
        textStyle = TextStyle.BODY_1,
        style = ButtonStyle.LOUD,
        onClick = OnClick.SAVE,
        size = ButtonSize.DEFAULT,
        arrangement = Arrangement.Start,
        modifier = Modifier
    ),
    medicines = listOf(infoCardUiModelMock),
    section = null,
    arrangement = Arrangement.Start,
    modifier = Modifier
)

val segmentedSwitchUiModelMock = SegmentedSwitchUiModel(
    identifier = "identifier",
    text = "text",
    textStyle = TextStyle.BODY_1,
    selected = true,
    options = listOf(
        OptionUiModel(
            text = "text",
            textStyle = TextStyle.BODY_1,
            color = "color"
        )
    ),
    arrangement = Arrangement.Start,
    modifier = Modifier
)

val signatureUiModelMock = SignatureUiModel(
    identifier = "identifier",
    signatureLabel = TextUiModel(
        text = "text",
        textStyle = TextStyle.BODY_1
    ),
    signatureButton = ButtonUiModel(
        identifier = "identifier",
        label = "text",
        textStyle = TextStyle.BODY_1,
        style = ButtonStyle.LOUD,
        onClick = OnClick.SAVE,
        size = ButtonSize.DEFAULT,
        arrangement = Arrangement.Start,
        modifier = Modifier
    ),
    signature = "signature",
    modifier = Modifier
)

val sliderUiModelMock = SliderUiModel(
    identifier = "identifier",
    min = 1,
    max = 10,
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
