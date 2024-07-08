package com.skgtecnologia.sisem.data.remote.interceptors

import android.content.Context
import com.skgtecnologia.sisem.commons.resources.ANDROID_NETWORKING_FILE_NAME
import com.skgtecnologia.sisem.commons.resources.StorageProvider
import com.valkiria.uicomponents.utlis.TimeUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.time.Instant
import javax.inject.Inject

class NetworkInterceptor @Inject constructor(
    private val storageProvider: StorageProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().run {
            newBuilder().build()
        }

        val requestContent = TimeUtils.getLocalDateTime(Instant.now()).toString() +
            "\t" + request.toString() +
            "\n"

        storageProvider.storeContent(
            ANDROID_NETWORKING_FILE_NAME,
            Context.MODE_APPEND,
            requestContent.toByteArray()
        )

        val response = chain.proceed(request)

        val responseContent = TimeUtils.getLocalDateTime(Instant.now()).toString() +
            "\t" + response.toString() +
            "\n"

        storageProvider.storeContent(
            ANDROID_NETWORKING_FILE_NAME,
            Context.MODE_APPEND,
            responseContent.toByteArray()
        )

        return response
    }
}
