package com.github.cythara.presentation.main

import com.github.cythara.domain.PitchDifference

interface MainView {
    fun updatePitchDifference(percent: PitchDifference?)

    fun updatePitchReference(value: Int)

    fun updateTuning(value: Int)

    fun updateRecordState(value: Boolean)

    fun updateNotation(value: Boolean)

    fun openNotationDialog(value: Int)

    fun openPitchReference(value: Int)
}
