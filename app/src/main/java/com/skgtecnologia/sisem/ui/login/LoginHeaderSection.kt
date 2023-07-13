package com.skgtecnologia.sisem.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.R

@Composable
fun LoginHeaderSection(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            painterResource(id = R.drawable.banner_sisem),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .padding(16.dp)
        )
    }
}
