package com.levp.catsandmath

import android.app.Application
import com.levp.catsandmath.presentation.common.di.ApplicationComponent
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CatsMathApp:Application() {
    companion object {
        lateinit var applicationComponent: ApplicationComponent
            private set
    }

    override fun onCreate() {
        super.onCreate()

    }
}