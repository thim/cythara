package com.github.cythara.domain

import java.util.*

object Sampler {

    fun calculateAverageDifference(samples: List<PitchDifference>): PitchDifference? {
        val mostFrequentNote = extractMostFrequentNote(samples)
        val filteredSamples = filterByNote(samples, mostFrequentNote)

        var deviationSum = 0.0
        var sameNoteCount = 0
        for (pitchDifference in filteredSamples) {
            deviationSum += pitchDifference.deviation
            sameNoteCount++
        }

        if (sameNoteCount > 0 && mostFrequentNote != null) {
            val averageDeviation = deviationSum / sameNoteCount
            return PitchDifference(mostFrequentNote, averageDeviation)
        }

        return null
    }

    private fun filterByNote(samples: List<PitchDifference>, note: Note?): List<PitchDifference> {
        val filteredSamples = ArrayList<PitchDifference>()

        for (sample in samples) {
            if (sample.closest === note) {
                filteredSamples.add(sample)
            }
        }

        return filteredSamples
    }

    private fun extractMostFrequentNote(samples: List<PitchDifference>): Note? {
        val noteFrequencies = HashMap<Note, Int>()

        for (pitchDifference in samples) {
            val closest = pitchDifference.closest
            if (noteFrequencies.containsKey(closest)) {
                val count = noteFrequencies[closest]
                noteFrequencies[closest] = count!! + 1
            } else {
                noteFrequencies[closest] = 1
            }
        }

        var mostFrequentNote: Note? = null
        var mostOccurrences = 0
        for (note in noteFrequencies.keys) {
            noteFrequencies[note]?.let {
                if (it > mostOccurrences) {
                    mostFrequentNote = note
                    mostOccurrences = it
                }
            }
        }

        return mostFrequentNote
    }
}
