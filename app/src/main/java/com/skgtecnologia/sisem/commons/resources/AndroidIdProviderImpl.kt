package com.skgtecnologia.sisem.commons.resources

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@Suppress("UnusedPrivateMember")
class AndroidIdProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AndroidIdProvider {

    override fun getAndroidId(): String = "asd123asd456" // FIXME: Hardcoded data
    // Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID).orEmpty()
}
