package com.github.cythara.domain

import java.util.*

object PitchComparator {
    fun retrieveNote(tuning: Tuning, pitch: Float): PitchDifference {

        fun log2(number: Float): Double = Math.log(number.toDouble()) / Math.log(2.0)

        Arrays.sort(tuning.notes) { o1, o2 -> java.lang.Float.compare(o1.frequency, o2.frequency) }

        var minCentDifference = Double.POSITIVE_INFINITY
        var closest = tuning.notes[0]

        tuning.notes.forEach {
            val centDifference = 1200.0 * log2(pitch / it.frequency)

            if (Math.abs(centDifference) < Math.abs(minCentDifference)) {
                minCentDifference = centDifference
                closest = it
            }
        }

        return PitchDifference(closest, minCentDifference)
    }
}