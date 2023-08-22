package com.skgtecnologia.sisem.ui.menu.header

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.R
import com.valkiria.uicomponents.props.TextStyle
import com.valkiria.uicomponents.props.toTextStyle
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
                painter = painterResource(id = it),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Column(
            modifier = Modifier
                .padding(start = 20.dp)
                .weight(0.6f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.name,
                textAlign = TextAlign.Start,
                style = TextStyle.HEADLINE_7.toTextStyle(),
                modifier = Modifier.height(20.dp)
            )
            Text(
                text = item.specialty,
                textAlign = TextAlign.Start,
                style = TextStyle.BUTTON_1.toTextStyle(),
                modifier = Modifier.height(20.dp)
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_logout),
            contentDescription = null,
            modifier = Modifier
                .padding(end = 20.dp)
                .clickable {
                    onClick.invoke()
                },
            tint = MaterialTheme.colorScheme.primary
        )
    }
}
