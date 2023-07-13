package com.skgtecnologia.sisem.ui.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowModel
import com.skgtecnologia.sisem.domain.model.bodyrow.ButtonModel
import com.skgtecnologia.sisem.domain.model.bodyrow.ChipModel
import com.skgtecnologia.sisem.domain.model.bodyrow.LabelModel
import com.skgtecnologia.sisem.domain.model.bodyrow.LabeledSwitchModel
import com.skgtecnologia.sisem.domain.model.bodyrow.PasswordTextFieldModel
import com.skgtecnologia.sisem.domain.model.bodyrow.RichLabelModel
import com.skgtecnologia.sisem.domain.model.bodyrow.TermsAndConditionsModel
import com.skgtecnologia.sisem.domain.model.bodyrow.TextFieldModel
import com.skgtecnologia.sisem.domain.model.bodyrow.mapToUiModel
import com.valkiria.uicomponents.components.chip.ChipComponent
import com.valkiria.uicomponents.props.button.ButtonComponent
import com.valkiria.uicomponents.props.textfield.TextFieldComponent
import com.valkiria.uicomponents.theme.montserratFontFamily

@Composable
fun BodySection(
    body: List<BodyRowModel>?,
    modifier: Modifier = Modifier
) {
    if (body?.isNotEmpty() == true) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            body.map { model ->
                when (model) {
                    is ButtonModel -> item(key = model.identifier) {
                        ButtonComponent(model = model.mapToUiModel())
                    }

                    is ChipModel -> item(key = model.identifier) {
                        ChipComponent(
                            uiModel = model.mapToUiModel(),
                            fontFamily = montserratFontFamily
                        )
                    }

                    is LabelModel -> item(key = model.identifier) {

                    }

                    is LabeledSwitchModel -> item(key = model.identifier) {

                    }

                    is PasswordTextFieldModel -> item(key = model.identifier) {

                    }

                    is RichLabelModel -> item(key = model.identifier) {

                    }

                    is TermsAndConditionsModel -> item(key = model.identifier) {

                    }

                    is TextFieldModel -> item(key = model.identifier) {
                        TextFieldComponent(model = model.mapToUiModel())
                    }
                }
            }
        }
    }
}
