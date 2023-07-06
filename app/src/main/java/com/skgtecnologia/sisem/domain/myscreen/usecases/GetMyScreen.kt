package com.skgtecnologia.sisem.domain.myscreen.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.myscreen.MyScreenRepository
import com.skgtecnologia.sisem.domain.myscreen.model.MyScreenModel
import javax.inject.Inject

class GetMyScreen @Inject constructor(
    private val myScreenRepository: MyScreenRepository
) {

    @CheckResult
    suspend operator fun invoke(myScreenIdentifier: Long): Result<MyScreenModel> = resultOf {
        val fetchResult = myScreenRepository.getMyScreen(myScreenIdentifier)

        fetchResult
    }
}
