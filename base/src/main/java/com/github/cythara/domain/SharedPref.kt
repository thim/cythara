package com.github.cythara.domain

import android.content.Context.MODE_PRIVATE
import com.github.cythara.CytharaApplication
import com.github.cythara.data.AppPreferences

class SharedPref : AppPreferences {

    private val preferences = CytharaApplication.instance.getSharedPreferences(PREFS_FILE, MODE_PRIVATE)

    override var isScientificNotation: Boolean
        get() = preferences.getBoolean(USE_SCIENTIFIC_NOTATION, false)
        set(value) {
            val editor = preferences.edit()
            editor.putBoolean(USE_SCIENTIFIC_NOTATION, value)
            editor.apply()
        }

    override var currentTuning: Int
        get() = preferences.getInt(CURRENT_TUNING, 0)
        set(value) {
            val editor = preferences.edit()
            editor.putInt(CURRENT_TUNING, value)
            editor.apply()
        }

    override var pitchReference: Int
        get() = preferences.getInt(REFERENCE_PITCH, 440)
        set(value) {
            val editor = preferences.edit()
            editor.putInt(REFERENCE_PITCH, value)
            editor.apply()
        }

    override fun useDarkMode(): Boolean {
        return preferences.getBoolean(USE_DARK_MODE, false)
    }

    override fun setDarkMode(value: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(USE_DARK_MODE, value)
        editor.apply()
    }

    companion object {
        private const val PREFS_FILE = "prefs_file"
        private const val USE_SCIENTIFIC_NOTATION = "use_scientific_notation"
        private const val CURRENT_TUNING = "current_tuning"
        private const val REFERENCE_PITCH = "reference_pitch"
        private const val USE_DARK_MODE = "use_dark_mode"
    }
}
