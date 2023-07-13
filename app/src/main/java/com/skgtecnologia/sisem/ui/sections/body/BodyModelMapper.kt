package com.skgtecnologia.sisem.ui.sections.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowModel
import com.skgtecnologia.sisem.domain.model.bodyrow.ButtonModel
import com.skgtecnologia.sisem.domain.model.bodyrow.ButtonModelNew
import com.skgtecnologia.sisem.domain.model.bodyrow.ChipModel
import com.skgtecnologia.sisem.domain.model.bodyrow.LabelModel
import com.skgtecnologia.sisem.domain.model.bodyrow.LabeledSwitchModel
import com.skgtecnologia.sisem.domain.model.bodyrow.PasswordTextFieldModel
import com.skgtecnologia.sisem.domain.model.bodyrow.RichLabelModel
import com.skgtecnologia.sisem.domain.model.bodyrow.TermsAndConditionsModel
import com.skgtecnologia.sisem.domain.model.bodyrow.TextFieldModel
import com.skgtecnologia.sisem.domain.model.bodyrow.mapToUiModel
import com.valkiria.uicomponents.components.button.ButtonComponent
import com.valkiria.uicomponents.components.textfield.TextFieldComponent

@Composable
fun BodyModelMapper(
    bodyRowModel: List<BodyRowModel>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        bodyRowModel.map { model ->
            when (model) {
                is ButtonModel -> {}

                is ButtonModelNew -> item {
                    ButtonComponent(model = model.mapToUiModel())
                }

                is ChipModel -> item {
//                    ChipComponent(model = model.mapToUiModel())
                }

                is LabelModel -> {}

                is LabeledSwitchModel -> {}


                is PasswordTextFieldModel -> {}

                is RichLabelModel -> item {
//                    LabelComponent(richLabelUiModel = model.mapToUiModel())
                }

                is TermsAndConditionsModel -> item {
                    // TermsAndConditions(...)
                }

                is TextFieldModel -> item {
                    TextFieldComponent(model = model.mapToUiModel())
                }
            }
        }
    }
}
