package com.skgtecnologia.sisem.ui.splash

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.skgtecnologia.sisem.ui.MainActivity.Companion.launchMainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@Suppress("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : ComponentActivity() {

    private val splashViewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setupSplashScreen()

        super.onCreate(savedInstanceState)
    }

    private fun setupSplashScreen() {
        var keepSplashScreenOn = true

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                splashViewModel.uiState.collect { startupUiState ->
                    keepSplashScreenOn = startupUiState.startupNavigationModel == null

                    if (keepSplashScreenOn.not()) {
                        val role = startupUiState.startupNavigationModel?.preOperationRole
                        Toast.makeText(
                            this@SplashActivity,
                            role?.humanizedName ?: "Fresh install",
                            Toast.LENGTH_LONG
                        ).show()

                        launchMainActivity(
                            this@SplashActivity,
                            startupUiState.startupNavigationModel
                        )
                        finish()
                    }
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val splashScreen = installSplashScreen()
            splashScreen.setKeepOnScreenCondition { keepSplashScreenOn }
        }
    }
}
