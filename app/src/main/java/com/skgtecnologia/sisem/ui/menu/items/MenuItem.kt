package com.skgtecnologia.sisem.ui.menu.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.ui.navigation.MainNavigationRoute
import com.valkiria.uicomponents.props.TextStyle
import com.valkiria.uicomponents.props.toTextStyle

@Composable
fun MenuItem(
    item: DrawerMenuItemModel,
    onClick: (route: MainNavigationRoute) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onClick(item.drawerOption) }
            .padding(vertical = 12.dp)
    ) {
        Icon(
            painter = painterResource(id = item.drawableId),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = item.title,
            modifier = Modifier.padding(start = 10.dp),
            style = TextStyle.HEADLINE_8.toTextStyle()
        )
    }
}
