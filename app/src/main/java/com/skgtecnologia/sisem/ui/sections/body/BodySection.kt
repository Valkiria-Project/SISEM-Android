package com.skgtecnologia.sisem.ui.sections.body

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.myscreen.model.BodyRowModel

@Composable
fun BodySection(
    body: List<BodyRowModel>?,
    modifier: Modifier = Modifier
) {
    if (body?.isNotEmpty() == true) {
        BodyModelMapper(
            bodyRowModel = body,
            modifier = modifier
        )
    }
}
