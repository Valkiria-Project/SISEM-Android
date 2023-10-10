package com.skgtecnologia.sisem.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.R

@Composable
fun LoginHeaderSection(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.banner_sisem),
            contentDescription = null,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.banner_gov),
            contentDescription = null,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}
