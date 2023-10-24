package com.valkiria.uicomponents.components.stepper

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.R.drawable
import com.valkiria.uicomponents.components.button.ButtonStyle
import com.valkiria.uicomponents.components.button.mapToColors
import com.valkiria.uicomponents.components.button.mapToTextColor
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.toTextStyle
import timber.log.Timber

@Composable
fun StepperComponent(
    uiModel: StepperUiModel,
    onAction: (id: String) -> Unit
) {
    Row(
        modifier = uiModel.modifier.fillMaxWidth(),
        horizontalArrangement = uiModel.arrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { /*TODO*/
            }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = drawable.ic_back),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(72.dp)
            )
        }

        Button(
            onClick = { onAction(uiModel.identifier) },
            colors = ButtonStyle.LOUD.mapToColors(),
            modifier = uiModel.modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = "FINALIZADO",
                color = ButtonStyle.LOUD.mapToTextColor(),
                style = TextStyle.HEADLINE_5.toTextStyle()
            )
        }

        IconButton(
            onClick = { /*TODO*/
            }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = drawable.ic_back),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(72.dp).rotate(180f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StepperComponentPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        StepperComponent(
            uiModel = StepperUiModel(
                identifier = "APH_STEPPER",
                options = emptyMap(),
                arrangement = Arrangement.Center,
                modifier = Modifier
            )
        ) {
            Timber.d("Stepper clicked")
        }
    }
}
