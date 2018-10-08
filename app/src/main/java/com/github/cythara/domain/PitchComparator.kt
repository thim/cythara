package com.github.cythara.domain

import java.util.Arrays

object PitchComparator {

    fun retrieveNote(tuning: Tuning, pitch: Float): PitchDifference {
        val notes = tuning.notes
        Arrays.sort(notes) { o1, o2 -> java.lang.Float.compare(o1.frequency, o2.frequency) }
        var minCentDifference = Double.POSITIVE_INFINITY

        var closest = notes[0]
        for (note in notes) {
            val centDifference = 1200.0 * log2(pitch / note.frequency)

            if (Math.abs(centDifference) < Math.abs(minCentDifference)) {
                minCentDifference = centDifference
                closest = note
            }
        }

        return PitchDifference(closest, minCentDifference)
    }

    private fun log2(number: Float): Double {
        return Math.log(number.toDouble()) / Math.log(2.0)
    }
}
