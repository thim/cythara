package com.github.cythara

import android.app.Application

class CytharaApplication : Application() {

    var darkModeEnabled: Boolean = false

    companion object {
        @JvmStatic
        lateinit var instance: CytharaApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}