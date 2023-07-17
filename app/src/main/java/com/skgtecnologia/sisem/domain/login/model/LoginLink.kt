package com.skgtecnologia.sisem.domain.login.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.domain.login.model.LoginLink.PRIVACY_POLICY
import com.skgtecnologia.sisem.domain.login.model.LoginLink.TERMS_AND_CONDITIONS
import com.valkiria.uicomponents.R.drawable
import com.valkiria.uicomponents.components.bottomsheet.BottomSheetUiModel
import com.valkiria.uicomponents.props.TextStyle
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
        titleTextStyle = TextStyle.HEADLINE_1,
        subtitle = stringResource(id = R.string.privacy_policy_subtitle),
        subtitleTextStyle = TextStyle.HEADLINE_6,
        text = stringResource(id = R.string.privacy_policy_title),
        textStyle = TextStyle.HEADLINE_5
    )

    TERMS_AND_CONDITIONS -> BottomSheetUiModel(
        icon = painterResource(id = drawable.ic_message),
        title = stringResource(id = R.string.terms_and_conditions_title),
        titleTextStyle = TextStyle.HEADLINE_1,
        subtitle = stringResource(id = R.string.terms_and_conditions_subtitle),
        subtitleTextStyle = TextStyle.HEADLINE_6,
        text = stringResource(id = R.string.terms_and_conditions_text),
        textStyle = TextStyle.HEADLINE_5
    )
}
