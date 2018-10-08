package com.github.cythara.data

interface AppPreferences {
    var isScientificNotation: Boolean
    var currentTuning: Int
    var pitchReference: Int

    fun useDarkMode(): Boolean
    fun setDarkMode(value: Boolean)
}
