package com.skgtecnologia.sisem.data.changepassword.remote

import com.skgtecnologia.sisem.core.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.core.data.remote.model.screen.ScreenResponse
import com.skgtecnologia.sisem.data.changepassword.remote.model.ChangePasswordBody
import com.skgtecnologia.sisem.data.changepassword.remote.model.ChangePasswordResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChangePasswordApi {

    @POST("screen/changePassword")
    suspend fun getChangePasswordScreen(
        @Body screenBody: ScreenBody
    ): Response<ScreenResponse>

    @POST("password/change-password")
    suspend fun changePassword(
        @Body changePasswordBody: ChangePasswordBody
    ): Response<ChangePasswordResponse>
}
