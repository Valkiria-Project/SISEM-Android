package com.valkiria.uicomponents.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.label.toTextStyle
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName

@Composable
fun SimpleCardComponent(
    uiModel: SimpleCardUiModel,
    onAction: (identifier: String) -> Unit
) {
    val iconResourceId = LocalContext.current.getResourceIdByName(
        uiModel.icon, DefType.DRAWABLE
    )

    val brush = Brush.horizontalGradient(
        colors = listOf(
            Color.Black,
            MaterialTheme.colorScheme.background
        )
    )

    ElevatedCard(
        modifier = uiModel.modifier
            .fillMaxWidth()
            .shadow(
                elevation = 25.dp,
                ambientColor = Color.Black,
                spotColor = Color.Black
            ),
        shape = RoundedCornerShape(20.dp),
    ) {
        Box(
            modifier = Modifier
                .clickable { onAction(uiModel.identifier) }
                .background(brush = brush)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(26.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                iconResourceId?.let {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = iconResourceId),
                        contentDescription = uiModel.title.text,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(40.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }

                Text(
                    text = uiModel.title.text,
                    style = uiModel.title.textStyle.toTextStyle(),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SimpleCardComponentPreview() {
    SimpleCardComponent(
        uiModel = SimpleCardUiModel(
            identifier = "identifier",
            icon = "ic_tools",
            title = TextUiModel(
                text = "Inventario herramientas del vehículo",
                textStyle = TextStyle.HEADLINE_4
            ),
            arrangement = Arrangement.Center,
            modifier = Modifier
        )
    ) { }
}
