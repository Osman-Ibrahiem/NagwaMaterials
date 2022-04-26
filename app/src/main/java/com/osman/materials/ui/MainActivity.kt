package com.osman.materials.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.osman.materials.R
import com.osman.materials.base.DefaultBaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : DefaultBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        var keepSplashOnScreen = true
        val delay = 2000L
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }
        Handler(Looper.getMainLooper()).postDelayed({ keepSplashOnScreen = false }, delay)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}