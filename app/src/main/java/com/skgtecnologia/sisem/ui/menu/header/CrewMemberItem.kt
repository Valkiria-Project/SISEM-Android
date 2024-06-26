package com.skgtecnologia.sisem.ui.menu.header

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.R
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.toTextStyle
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName

@Suppress("LongMethod", "MagicNumber")
@Composable
fun CrewMemberItem(item: CrewMemberMenuItemModel, onClick: () -> Unit) {
    val iconResourceId = LocalContext.current.getResourceIdByName(
        item.drawableProfession, DefType.DRAWABLE
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        iconResourceId?.let {
            Icon(
                imageVector = ImageVector.vectorResource(id = it),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }

        Column(
            modifier = Modifier
                .padding(start = 20.dp, end = 8.dp)
                .weight(0.6f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.name,
                textAlign = TextAlign.Start,
                style = TextStyle.HEADLINE_7.toTextStyle(),
                modifier = Modifier.wrapContentHeight()
            )
            Text(
                text = item.specialtyAndDocument,
                textAlign = TextAlign.Start,
                style = TextStyle.BUTTON_1.toTextStyle(),
                modifier = Modifier.wrapContentHeight()
            )
        }

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_logout),
            contentDescription = null,
            modifier = Modifier
                .padding(end = 4.dp)
                .clickable {
                    onClick.invoke()
                },
            tint = MaterialTheme.colorScheme.primary
        )
    }
}
