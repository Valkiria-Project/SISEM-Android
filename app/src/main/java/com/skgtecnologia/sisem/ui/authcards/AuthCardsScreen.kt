package com.skgtecnologia.sisem.ui.authcards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.ui.navigation.AuthNavigationRoute
import com.valkiria.uicomponents.components.card.CardComponent

@Composable
fun AuthCardsScreen(
    isTablet: Boolean,
    modifier: Modifier = Modifier,
    onNavigation: (route: AuthNavigationRoute) -> Unit
) {
    LazyColumn(
        modifier = modifier.clickable {
            onNavigation(AuthNavigationRoute.Login)
        },
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            CardComponent(isNews = true, modifier = modifier)
        }

        item {
            CardComponent(isNews = true, isLogin = true, modifier = modifier)
        }
    }
}
