package com.skgtecnologia.sisem.domain

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.valkiria.uicomponents.components.textfield.ValidationUiModel
import javax.inject.Inject
import timber.log.Timber

class ValidateInvalidInput @Inject constructor() {

    @CheckResult
    operator fun invoke(
        value: String, validations: List<ValidationUiModel>?
    ): Result<Boolean> = resultOf {
        val invalidRegex = validations?.find {
            value.matches(it.regex.toRegex()).not()
        }

        if (invalidRegex != null) {
            Timber.wtf("There is a validation that was not met for the given value")
            true
        } else {
            false
        }
    }
}
