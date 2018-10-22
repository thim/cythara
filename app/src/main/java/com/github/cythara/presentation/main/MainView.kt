package com.github.cythara.presentation.main

import com.github.cythara.domain.PitchDifference

interface MainView {
    fun updatePitchDifference(value: PitchDifference?)

    fun updatePitchReference(value: Int)

    fun updateTuning(value: Int, reload: Boolean)

    fun updateRecordState(value: Boolean)

    fun updateNotation(value: Boolean)

    fun openNotationDialog(value: Int)

    fun openPitchReference(value: Int)
}
