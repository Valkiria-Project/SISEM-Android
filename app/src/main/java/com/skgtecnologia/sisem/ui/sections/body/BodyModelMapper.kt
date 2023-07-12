package com.skgtecnologia.sisem.ui.sections.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.domain.myscreen.model.BodyRowModel
import com.skgtecnologia.sisem.domain.myscreen.model.ButtonModelNew
import com.skgtecnologia.sisem.domain.myscreen.model.ChipModel
import com.skgtecnologia.sisem.domain.myscreen.model.CrossSellingModel
import com.skgtecnologia.sisem.domain.myscreen.model.MessageModel
import com.skgtecnologia.sisem.domain.myscreen.model.PaymentMethodInfoModel
import com.skgtecnologia.sisem.domain.myscreen.model.RichLabelModel
import com.skgtecnologia.sisem.domain.myscreen.model.TermsAndConditionsModel
import com.skgtecnologia.sisem.domain.myscreen.model.TextFieldModel
import com.skgtecnologia.sisem.domain.myscreen.model.mapToUiModel
import com.valkiria.uicomponents.components.button.ButtonComponent
import com.valkiria.uicomponents.components.chip.ChipComponent
import com.valkiria.uicomponents.components.textfield.TextFieldComponent
import com.valkiria.uicomponents.payments.PaymentMethodInfo

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
                is CrossSellingModel -> item {
                    // CrossSelling(...)
                    Text(
                        text = model.text,
                        color = Color.Black.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center
                    )
                }

                is MessageModel -> item {
                    // Message(...)
                    Text(
                        text = model.text,
                        color = Color.Black.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center
                    )
                }

                is PaymentMethodInfoModel -> item {
                    PaymentMethodInfo(
                        imageUrl = "",
                        amountPaid = 100.0,
                        discount = "50% OFF",
                        rawAmount = 100.0,
                        methodType = model.methodType
                    )
                }

                is ChipModel -> item {
                    ChipComponent(model = model.mapToUiModel())
                }

                is RichLabelModel -> item {
//                    LabelComponent(richLabelUiModel = model.mapToUiModel())
                }

                is TextFieldModel -> item {
                    TextFieldComponent(model = model.mapToUiModel())
                }

                is ButtonModelNew -> item {
                    ButtonComponent(model = model.mapToUiModel())
                }

                is TermsAndConditionsModel -> item {
                    // TermsAndConditions(...)
                }
            }
        }
    }
}
