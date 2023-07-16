package com.skgtecnologia.sisem.domain.login.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.domain.login.model.LoginLink.PRIVACY_POLICY
import com.skgtecnologia.sisem.domain.login.model.LoginLink.TERMS_AND_CONDITIONS
import com.valkiria.uicomponents.R.drawable
import com.valkiria.uicomponents.components.bottomsheet.BottomSheetUiModel
import com.valkiria.uicomponents.props.TextStyle.HEADLINE_1
import com.valkiria.uicomponents.props.TextStyle.HEADLINE_5
import java.util.Locale

enum class LoginLink {
    PRIVACY_POLICY,
    TERMS_AND_CONDITIONS;

    companion object {
        fun getLinkByName(link: String): LoginLink = enumValueOf(link.uppercase(Locale.ROOT))
    }
}

@Composable
fun LoginLink.toBottomSheetUiModel() = when (this) {
    PRIVACY_POLICY -> BottomSheetUiModel(
        icon = painterResource(id = drawable.ic_message),
        title = stringResource(id = R.string.privacy_policy_title),
        titleTextStyle = HEADLINE_1,
        text = stringResource(id = R.string.privacy_policy_title),
        textStyle = HEADLINE_5
    )

    TERMS_AND_CONDITIONS -> BottomSheetUiModel(
        icon = painterResource(id = drawable.ic_message),
        title = stringResource(id = R.string.terms_and_conditions_title),
        titleTextStyle = HEADLINE_1,
        text = stringResource(id = R.string.terms_and_conditions_title),
        textStyle = HEADLINE_5
    )
}
