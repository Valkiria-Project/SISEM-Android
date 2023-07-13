package com.valkiria.uicomponents.props.chip

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.valkiria.uicomponents.R

@Composable
fun ChipComponent(
    model: ChipUiModel,
    modifier: Modifier = Modifier
) {
    model.text?.let {
        AssistChip(
            onClick = { },
            label = { Text(text = it) },
            leadingIcon =  {
                Icon(
                    painter = painterResource(id = R.drawable.ic_ambulancia),
                    contentDescription = "recurso test",
                    modifier = modifier.size(AssistChipDefaults.IconSize)
                )
            }
        )
    }
}
