package com.skgtecnologia.sisem.ui.forgotpassword

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

@Suppress("UnusedPrivateMember")
@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    onNavigation: (forgotPasswordNavigationModel: NavigationModel?) -> Unit
) {
    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        Text("...")
    }
}
