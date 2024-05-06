package com.skgtecnologia.sisem.commons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.bricks.banner.report.ReportDetailResponse
import com.skgtecnologia.sisem.data.remote.model.bricks.banner.report.ReportsDetailResponse
import com.skgtecnologia.sisem.data.remote.model.bricks.chip.ChipSectionResponse
import com.skgtecnologia.sisem.data.remote.model.components.body.HumanBodyResponse
import com.skgtecnologia.sisem.data.remote.model.components.body.WoundResponse
import com.skgtecnologia.sisem.data.remote.model.components.button.ButtonResponse
import com.skgtecnologia.sisem.data.remote.model.components.button.ImageButtonOptionResponse
import com.skgtecnologia.sisem.data.remote.model.components.button.ImageButtonResponse
import com.skgtecnologia.sisem.data.remote.model.components.button.ImageButtonSectionResponse
import com.skgtecnologia.sisem.data.remote.model.components.card.InfoCardResponse
import com.skgtecnologia.sisem.data.remote.model.components.card.PillResponse
import com.skgtecnologia.sisem.data.remote.model.components.card.SimpleCardResponse
import com.skgtecnologia.sisem.data.remote.model.components.chip.ChipOptionResponse
import com.skgtecnologia.sisem.data.remote.model.components.chip.ChipOptionsResponse
import com.skgtecnologia.sisem.data.remote.model.components.chip.ChipSelectionItemResponse
import com.skgtecnologia.sisem.data.remote.model.components.chip.ChipSelectionResponse
import com.skgtecnologia.sisem.data.remote.model.components.footer.FooterResponse
import com.skgtecnologia.sisem.data.remote.model.components.header.HeaderResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.ListPatientResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.ListTextResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.TextResponse
import com.skgtecnologia.sisem.domain.login.model.LoginIdentifier
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.preoperational.model.Novelty
import com.skgtecnologia.sisem.domain.report.model.ImageModel
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
import java.io.File

const val ANDROID_ID = "123"
const val SERVER_ERROR_TITLE = "Error en servidor"
const val USERNAME = "username"
const val PASSWORD = "password"
const val SERIAL = "serial"
const val CODE = "123"
const val EMAIL = "email"
const val TOPIC = "topic"
const val DESCRIPTION = "description"
const val TURN_ID = "1"
const val ID_APH = 14
const val DRIVER_ROLE = "DRIVER"

val uiAction = FooterUiAction.FooterButton(LoginIdentifier.LOGIN_RE_AUTH_BANNER.name)
val emptyScreenModel = ScreenModel(body = emptyList())
val imageModelMock = ImageModel(
    fileName = "fileName",
    file = File("file")
)
val noveltyMock = Novelty(
    idPreoperational = "1",
    novelty = "novelty",
    images = listOf(imageModelMock)
)

val chipOptionsUiModelMock = ChipOptionsUiModel(
    identifier = "identifier",
    items = listOf(
        ChipOptionUiModel(
            id = "identifier",
            name = "text",
            selected = true
        )
    ),
    required = true,
    visibility = true,
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
    selectionVisibility = mapOf(),
    deselectionVisibility = mapOf(),
    arrangement = Arrangement.Start,
    modifier = Modifier
)

val dropDownUiModelMock = DropDownUiModel(
    identifier = "identifier",
    label = "text",
    items = listOf(
        DropDownItemUiModel(
            id = "identifier",
            name = "name"
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
    selected = "name",
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

val headerResponseMock = HeaderResponse(
    identifier = "identifier",
    title = TextResponse(
        text = "text",
        textStyle = TextStyle.BODY_1
    ),
    subtitle = TextResponse(
        text = "text",
        textStyle = TextStyle.BODY_1
    ),
    leftIcon = "ic_algo",
    rightIcon = "ic_algo",
    badgeCount = "badgeCount",
    visibility = true,
    required = false,
    modifier = Modifier
)

val buttonResponseMock = ButtonResponse(
    identifier = "identifier",
    leftIcon = "ic_algo",
    label = "text",
    textStyle = TextStyle.BODY_1,
    style = ButtonStyle.LOUD,
    onClick = OnClick.SAVE,
    size = ButtonSize.DEFAULT,
    visibility = true,
    required = false,
    arrangement = Arrangement.Start,
    modifier = Modifier
)

val chipOptionsResponseMock = ChipOptionsResponse(
    identifier = "identifier",
    title = TextResponse(
        text = "text",
        textStyle = TextStyle.BODY_1
    ),
    items = listOf(
        ChipOptionResponse(
            id = "id",
            name = "name",
            selected = false
        )
    ),
    required = true,
    visibility = true,
    arrangement = Arrangement.Start,
    modifier = Modifier,
    selectionVisibility = null,
    deselectionVisibility = null
)

val chipSelectionResponseMock = ChipSelectionResponse(
    identifier = "identifier",
    title = TextResponse(
        text = "text",
        textStyle = TextStyle.BODY_1
    ),
    items = listOf(
        ChipSelectionItemResponse(
            id = "id",
            name = "name"
        )
    ),
    selected = "selected",
    selectionVisibility = mapOf(),
    deselectionVisibility = mapOf(),
    visibility = true,
    required = false,
    arrangement = Arrangement.Start,
    modifier = Modifier
)

val humanBodyResponseMock = HumanBodyResponse(
    identifier = "identifier",
    header = headerResponseMock,
    values = mapOf(
        "FRONT" to listOf(
            WoundResponse(
                type = "FRONT",
                area = "area",
                areaName = "areaName",
                wounds = listOf("wounds")
            )
        )
    ),
    wounds = listOf("wounds"),
    burningLevel = listOf("burningLevel"),
    section = "",
    visibility = true,
    required = false,
    arrangement = Arrangement.Start,
    modifier = Modifier
)

val imageButtonResponseMock = ImageButtonResponse(
    identifier = "identifier",
    title = TextResponse(
        text = "text",
        textStyle = TextStyle.BODY_1
    ),
    image = "image",
    selected = false,
    visibility = true,
    required = false,
    arrangement = Arrangement.Start,
    modifier = Modifier
)

val infoCardResponseMock = InfoCardResponse(
    identifier = "identifier",
    icon = "icon",
    title = TextResponse(
        text = "text",
        textStyle = TextStyle.BODY_1
    ),
    pill = PillResponse(
        title = TextResponse(
            text = "text",
            textStyle = TextStyle.BODY_1
        ),
        color = "color",
        leftIcon = null
    ),
    date = TextResponse(
        text = "text",
        textStyle = TextStyle.BODY_1
    ),
    chipSection = ChipSectionResponse(
        title = TextResponse(
            text = "text",
            textStyle = TextStyle.BODY_1
        ),
        listText = ListTextResponse(
            texts = listOf("texts"),
            textStyle = TextStyle.BODY_1
        ),
        listPatient = ListPatientResponse(
            texts = mapOf("1" to "texts"),
            textStyle = TextStyle.BODY_1,
            icon = "icon"
        )
    ),
    reportsDetail = ReportsDetailResponse(
        header = headerResponseMock,
        details = listOf(
            ReportDetailResponse(
                images = listOf("images"),
                title = TextResponse(
                    text = "text",
                    textStyle = TextStyle.BODY_1
                ),
                subtitle = TextResponse(
                    text = "text",
                    textStyle = TextStyle.BODY_1
                ),
                description = TextResponse(
                    text = "text",
                    textStyle = TextStyle.BODY_1
                ),
                modifier = Modifier
            )
        )
    ),
    visibility = true,
    required = false,
    arrangement = Arrangement.Start,
    modifier = Modifier
)

val imageButtonSectionResponseMock = ImageButtonSectionResponse(
    identifier = "identifier",
    options = listOf(
        ImageButtonOptionResponse(
            identifier = "identifier",
            title = TextResponse(
                text = "text",
                textStyle = TextStyle.BODY_1
            ),
            options = listOf(
                ImageButtonResponse(
                    identifier = "identifier",
                    title = TextResponse(
                        text = "text",
                        textStyle = TextStyle.BODY_1
                    ),
                    image = "image",
                    selected = false,
                    visibility = true,
                    required = false,
                    arrangement = Arrangement.Start,
                    modifier = Modifier
                )
            ),
            modifier = Modifier
        )
    ),
    visibility = true,
    required = false,
    arrangement = Arrangement.Start,
    modifier = Modifier
)

val simpleCardResponseMock = SimpleCardResponse(
    identifier = "identifier",
    title = TextResponse(
        text = "text",
        textStyle = TextStyle.BODY_1
    ),
    icon = "icon",
    visibility = true,
    required = false,
    arrangement = Arrangement.Start,
    modifier = Modifier
)

val footerResponseMock = FooterResponse(
    leftButton = ButtonResponse(
        identifier = "identifier",
        leftIcon = "ic_algo",
        label = "text",
        textStyle = TextStyle.BODY_1,
        style = ButtonStyle.LOUD,
        onClick = OnClick.SAVE,
        size = ButtonSize.DEFAULT,
        visibility = true,
        required = false,
        arrangement = Arrangement.Start,
        modifier = Modifier
    ),
    rightButton = ButtonResponse(
        identifier = "identifier",
        leftIcon = "ic_algo",
        label = "text",
        textStyle = TextStyle.BODY_1,
        style = ButtonStyle.LOUD,
        onClick = OnClick.SAVE,
        size = ButtonSize.DEFAULT,
        visibility = true,
        required = false,
        arrangement = Arrangement.Start,
        modifier = Modifier
    )
)
