package com.skgtecnologia.sisem.ui.sendemail

import android.graphics.Color.parseColor
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.domain.model.label.sendEmailContent
import com.skgtecnologia.sisem.domain.model.label.sendEmailLabels
import com.skgtecnologia.sisem.domain.model.textfield.sendEmailTextField
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import com.valkiria.uicomponents.bricks.textfield.OutlinedTextFieldView
import com.valkiria.uicomponents.components.label.LabelComponent
import com.valkiria.uicomponents.components.textfield.InputUiModel
import kotlinx.coroutines.launch

@Suppress("LongMethod")
@Composable
fun SendEmailScreen(
    modifier: Modifier = Modifier,
    idAph: String,
    onNavigation: (sendEmailNavigationModel: SendEmailNavigationModel) -> Unit
) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<SendEmailViewModel>()
    val uiState = viewModel.uiState

    LaunchedEffect(uiState) {
        launch {
            when {
                uiState.navigationModel != null -> {
                    viewModel.consumeNavigationEvent()
                    onNavigation(uiState.navigationModel)
                }
            }
        }
    }

    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (header, body) = createRefs()

        Row(
            modifier = Modifier
                .padding(20.dp)
                .constrainAs(header) {
                    top.linkTo(parent.top)
                }
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(
                    id = com.valkiria.uicomponents.R.drawable.ic_close
                ),
                contentDescription = null,
                modifier = Modifier
                    .clickable { viewModel.cancel() }
                    .padding(end = 12.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Icon(
                imageVector = ImageVector.vectorResource(
                    id = com.valkiria.uicomponents.R.drawable.ic_send
                ),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        viewModel.bodyValue.value = context.getString(R.string.send_email_body)
                        viewModel.sendEmail(idAph)
                    }
                    .padding(end = 12.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Column(
            modifier = Modifier
                .padding(20.dp)
                .constrainAs(body) {
                    top.linkTo(header.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
        ) {
            OutlinedTextFieldView(
                uiModel = sendEmailTextField(),
                validateFields = uiState.validateFields,
                onAction = { id, updatedValue, fieldValidated, _ ->
                    viewModel.emailValue.value = InputUiModel(
                        identifier = id,
                        updatedValue = updatedValue,
                        fieldValidated = fieldValidated
                    )
                }
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                thickness = 1.dp,
                color = Color(parseColor("#3D3F42"))
            )

            LabelComponent(uiModel = sendEmailLabels(context.getString(R.string.send_email_from)))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                thickness = 1.dp,
                color = Color(parseColor("#3D3F42"))
            )

            LabelComponent(
                uiModel = sendEmailLabels(context.getString(R.string.send_email_body))
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                thickness = 1.dp,
                color = Color(parseColor("#3D3F42"))
            )

            LabelComponent(
                uiModel = sendEmailLabels(context.getString(R.string.send_email_content), "#FFFFFF")
            )

            LabelComponent(
                uiModel = sendEmailContent(context.getString(R.string.send_email_content_pdf))
            )
        }
    }

    OnBannerHandler(uiModel = uiState.infoEvent) {
        viewModel.navigateToMain()
    }

    OnBannerHandler(uiModel = uiState.errorEvent) { uiAction ->
        viewModel.handleEvent(uiAction)
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}
