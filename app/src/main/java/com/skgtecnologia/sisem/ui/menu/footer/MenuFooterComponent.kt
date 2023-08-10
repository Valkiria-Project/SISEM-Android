package com.skgtecnologia.sisem.ui.menu.footer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.R.drawable

@Composable
fun MenuFooterComponent(modifier: Modifier) {
    Row(modifier = modifier) {
        Image(
            painter = painterResource(id = drawable.banner_sisem),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
        )

        Image(
            painter = painterResource(id = drawable.banner_gov),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 25.dp)
        )
    }
}
