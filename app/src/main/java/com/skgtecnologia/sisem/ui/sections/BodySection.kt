package com.skgtecnologia.sisem.ui.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.domain.login.model.LoginIdentifier
import com.skgtecnologia.sisem.domain.model.body.BodyRowModel
import com.skgtecnologia.sisem.domain.model.body.ButtonModel
import com.skgtecnologia.sisem.domain.model.body.ChipModel
import com.skgtecnologia.sisem.domain.model.body.LabelModel
import com.skgtecnologia.sisem.domain.model.body.LabeledSwitchModel
import com.skgtecnologia.sisem.domain.model.body.PasswordTextFieldModel
import com.skgtecnologia.sisem.domain.model.body.RichLabelModel
import com.skgtecnologia.sisem.domain.model.body.TermsAndConditionsModel
import com.skgtecnologia.sisem.domain.model.body.TextFieldModel
import com.skgtecnologia.sisem.domain.model.body.mapToUiModel
import com.valkiria.uicomponents.components.button.ButtonComponent
import com.valkiria.uicomponents.components.chip.ChipComponent
import com.valkiria.uicomponents.components.label.LabelComponent
import com.valkiria.uicomponents.components.passwordtextfield.PasswordTextFieldComponent
import com.valkiria.uicomponents.components.richlabel.RichLabelComponent
import com.valkiria.uicomponents.components.termsandconditions.TermsAndConditionsComponent
import com.valkiria.uicomponents.components.textfield.TextFieldComponent

@Suppress("LongMethod")
@Composable
fun BodySection(
    body: List<BodyRowModel>?,
    isTablet: Boolean,
    modifier: Modifier = Modifier,
    onClick: (link: String) -> Unit
) {
    val context = LocalContext.current

    if (body?.isNotEmpty() == true) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            body.map { model ->
                when (model) {
                    is ButtonModel -> item(key = model.identifier) {
                        when (model.identifier) {
                            LoginIdentifier.LOGIN_BUTTON.name -> ButtonComponent(
                                uiModel = model.mapToUiModel(),
                                isTablet = isTablet
                            )

                            LoginIdentifier.LOGIN_FORGOT_PASSWORD_BUTTON.name -> ButtonComponent(
                                uiModel = model.mapToUiModel(),
                                isTablet = isTablet,
                                arrangement = Arrangement.Start
                            )
                        }
                    }

                    is ChipModel -> item(key = model.identifier) {
                        ChipComponent(
                            uiModel = model.mapToUiModel()
                        )
                    }

                    is LabelModel -> item(key = model.identifier) {
                        LabelComponent(uiModel = model.mapToUiModel())
                    }

                    is LabeledSwitchModel -> item(key = model.identifier) {

                    }

                    is PasswordTextFieldModel -> item(key = model.identifier) {
                        PasswordTextFieldComponent(
                            uiModel = model.mapToUiModel(),
                            isTablet = isTablet
                        )
                    }

                    is RichLabelModel -> item(key = model.identifier) {
                        RichLabelComponent(uiModel = model.mapToUiModel())
                    }

                    is TermsAndConditionsModel -> item(key = model.identifier) {
                        TermsAndConditionsComponent(
                            uiModel = model.mapToUiModel(),
                            isTablet = isTablet
                        ) { link ->
                            onClick(link)
                        }
                    }

                    is TextFieldModel -> item(key = model.identifier) {
                        TextFieldComponent(
                            uiModel = model.mapToUiModel(),
                            isTablet = isTablet
                        )
                    }
                }
            }
        }
    }
}
