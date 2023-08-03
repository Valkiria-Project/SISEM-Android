package com.skgtecnologia.sisem.ui.preoperational

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import com.skgtecnologia.sisem.ui.navigation.NavigationGraph

@Suppress("LongMethod")
@Composable
fun PreOperationalScreen(
    isTablet: Boolean,
    modifier: Modifier = Modifier,
    onNavigation: (route: NavigationGraph) -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .clickable {
                onNavigation(NavigationGraph.Main)
            }
    ) {
        val (header, body) = createRefs()


    }
}
