package com.github.cythara.domain

import java.util.*

object Sampler {

    @JvmStatic
    fun calculateAverageDifference(samples: List<PitchDifference>): PitchDifference? {
        val mostFrequentNote = extractMostFrequentNote(samples)

        var deviationSum = 0.0
        var sameNoteCount = 0

        samples.filter { it.closest == mostFrequentNote }.forEach {
            deviationSum += it.deviation
            sameNoteCount++
        }

        if (sameNoteCount > 0 && mostFrequentNote != null) {
            val averageDeviation = deviationSum / sameNoteCount
            return PitchDifference(mostFrequentNote, averageDeviation)
        }

        return null
    }

    @JvmStatic
    fun extractMostFrequentNote(samples: List<PitchDifference>): Note? {
        val noteFrequencies = HashMap<Note, Int>()
        samples.forEach {
            val count = noteFrequencies[it.closest] ?: 0
            noteFrequencies[it.closest] = count + 1
        }

        return noteFrequencies.maxBy { it.value }?.key
    }
}
