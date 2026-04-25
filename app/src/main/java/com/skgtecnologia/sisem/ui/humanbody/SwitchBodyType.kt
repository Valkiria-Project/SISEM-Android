package com.skgtecnologia.sisem.ui.humanbody

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.components.label.toTextStyle

@Composable
fun SwitchBodyType(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_rotate),
            contentDescription = null,
            modifier = Modifier
                .clickable { onClick() }
                .size(32.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            modifier = Modifier
                .padding(top = 4.dp),
            text = stringResource(R.string.human_body_label),
            style = com.valkiria.uicomponents.components.label.TextStyle.HEADLINE_4.toTextStyle()
        )
    }
}
