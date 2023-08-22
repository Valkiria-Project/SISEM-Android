package com.skgtecnologia.sisem.ui.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.R
import com.valkiria.uicomponents.components.button.ButtonUiModel
import com.valkiria.uicomponents.components.button.ButtonView
import com.valkiria.uicomponents.components.button.OnClick
import com.valkiria.uicomponents.components.label.LabelComponent
import com.valkiria.uicomponents.components.label.LabelUiModel
import com.valkiria.uicomponents.components.textfield.TextFieldComponent
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import com.valkiria.uicomponents.components.textfield.ValidationUiModel
import com.valkiria.uicomponents.props.ButtonSize
import com.valkiria.uicomponents.props.ButtonStyle
import com.valkiria.uicomponents.props.TextStyle
import timber.log.Timber

@Suppress("LongMethod")
@Composable
fun FindingFormContent() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        LabelComponent(
            uiModel = LabelUiModel(
                text = stringResource(R.string.finding_input_label),
                textStyle = TextStyle.HEADLINE_6,
                arrangement = Arrangement.Start
            )
        )

        TextFieldComponent(
            uiModel = TextFieldUiModel(
                label = stringResource(R.string.finding_input_hint),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                textStyle = TextStyle.HEADLINE_8,
                validations = listOf(
                    ValidationUiModel(
                        regex = "^(?!\\s*$).+",
                        message = stringResource(R.string.field_empty_validation_message)
                    )
                ),
                arrangement = Arrangement.Start,
                modifier = Modifier.padding(top = 8.dp)
            )
        ) { updatedValue, fieldValidated ->
            Timber.d("Propagate this value $updatedValue upstream")
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End)
        ) {
            ButtonView(
                uiModel = ButtonUiModel(
                    label = stringResource(R.string.finding_input_save_cta),
                    style = ButtonStyle.LOUD,
                    textStyle = TextStyle.BUTTON_1,
                    onClick = OnClick.FORGOT_PASSWORD, // FIXME: Use the onClick
                    size = ButtonSize.DEFAULT,
                    arrangement = Arrangement.Center,
                    modifier = Modifier
                )
            ) {
                Timber.d("Propagate this clicked button upstream")
            }

            ButtonView(
                uiModel = ButtonUiModel(
                    label = stringResource(R.string.finding_input_save_cta),
                    style = ButtonStyle.LOUD,
                    textStyle = TextStyle.BUTTON_1,
                    onClick = OnClick.FORGOT_PASSWORD, // FIXME: Use the onClick
                    size = ButtonSize.DEFAULT,
                    arrangement = Arrangement.Center,
                    modifier = Modifier
                )
            ) {
                Timber.d("Propagate this clicked button upstream")
            }
        }
    }
}
