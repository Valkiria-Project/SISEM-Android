package com.skgtecnologia.sisem.ui.preoperational

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.di.operation.OperationRole

@Composable
fun PreOperationalByRoleScreen(
    modifier: Modifier = Modifier,
    role: OperationRole?
) {
    Text(text = "Role: ${role?.humanizedName}")
}
