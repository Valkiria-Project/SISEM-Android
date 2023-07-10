package com.skgtecnologia.sisem.ui.loginscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.skgtecnologia.sisem.R

@Composable
fun LoginHeaderSection(
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier.fillMaxWidth()
            .background(color = Color(0xFF242426)),
        painter = painterResource(id = R.drawable.ic_banner),
        contentDescription = "Banner de sisem",
        contentScale = ContentScale.FillWidth
    )
}
