package com.skgtecnologia.sisem.domain.myscreen.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.commons.resources.StringLookup
import com.skgtecnologia.sisem.domain.myscreen.MyScreenRepository
import validateOrThrow
import javax.inject.Inject

class FetchMyScreen @Inject constructor(
    private val myScreenRepository: MyScreenRepository,
    private val stringLookup: StringLookup
) {

    @CheckResult
    suspend operator fun invoke(shouldFail: Boolean): Result<Long> = resultOf {
        validateOrThrow(!shouldFail) {
            throw InputError(stringLookup.getString(R.string.fetch_input_error_message))
        }

        val fetchResult = myScreenRepository.fetchMyScreen()

        validateOrThrow(fetchResult > 0) {
            ResultError(stringLookup.getString(R.string.fetch_result_error_message))
        }

        fetchResult
    }
}

sealed interface DoSomethingError
data class InputError(val errorMessage: String) : DoSomethingError, Exception(errorMessage)
data class ResultError(val errorMessage: String) : DoSomethingError, Exception(errorMessage)
