package com.skgtecnologia.sisem.domain.login.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.domain.login.model.LoginLink.PRIVACY_POLICY
import com.skgtecnologia.sisem.domain.login.model.LoginLink.TERMS_AND_CONDITIONS
import com.valkiria.uicomponents.R.drawable
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.ui.body.TermsAndConditionsUiModel
import com.valkiria.uicomponents.model.ui.link.Link
import java.util.Locale

enum class LoginLink : Link {
    PRIVACY_POLICY,
    TERMS_AND_CONDITIONS;

    companion object {
        fun getLinkByName(link: String): LoginLink = enumValueOf(link.uppercase(Locale.ROOT))
    }
}

@Composable
fun LoginLink.toLegalContentModel() = when (this) {
    PRIVACY_POLICY -> LegalContentModel(
        icon = painterResource(id = drawable.ic_message),
        title = stringResource(id = R.string.privacy_policy_title),
        titleTextStyle = TextStyle.HEADLINE_1,
        subtitle = stringResource(id = R.string.privacy_policy_subtitle),
        subtitleTextStyle = TextStyle.HEADLINE_6,
        text = stringResource(id = R.string.privacy_policy_text),
        textStyle = TextStyle.HEADLINE_5
    )

    TERMS_AND_CONDITIONS -> LegalContentModel(
        icon = painterResource(id = drawable.ic_message),
        title = stringResource(id = R.string.terms_and_conditions_title),
        titleTextStyle = TextStyle.HEADLINE_1,
        subtitle = stringResource(id = R.string.terms_and_conditions_subtitle),
        subtitleTextStyle = TextStyle.HEADLINE_6,
        text = stringResource(id = R.string.terms_and_conditions_text),
        textStyle = TextStyle.HEADLINE_5
    )
}

fun TermsAndConditionsUiModel.mapToLoginModel(): TermsAndConditionsUiModel {
    return TermsAndConditionsUiModel(
        identifier = identifier,
        links = listOf(
            TERMS_AND_CONDITIONS,
            PRIVACY_POLICY
        ),
        arrangement = arrangement,
        modifier = modifier
    )
}
