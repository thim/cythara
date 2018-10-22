package com.github.cythara

import com.github.cythara.domain.Note
import com.github.cythara.domain.PitchDifference
import com.github.cythara.domain.Sampler
import org.hamcrest.CoreMatchers.either
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.closeTo
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThat
import org.junit.Test
import java.util.*

class SamplerTest {

    @Test
    fun the_average_difference_is_calculated_correctly() {
        val samples = ArrayList<PitchDifference>()

        samples.add(PitchDifference(Note.createNote("E2"), 2.46))
        samples.add(PitchDifference(Note.createNote("E2"), -10.3))
        samples.add(PitchDifference(Note.createNote("E2"), 5.71))
        samples.add(PitchDifference(Note.createNote("E2"), 12.532))
        samples.add(PitchDifference(Note.createNote("E2"), -0.414))

        val pitchDifference = Sampler.calculateAverageDifference(samples)

        val average = (2.46 - 10.3 + 5.71 + 12.532 - 0.414) / 5.0

        assertNotNull(pitchDifference)
        assertThat(pitchDifference!!.closest.name, `is`("E"))
        assertThat(pitchDifference.deviation, closeTo(average, 0.001))
    }

    @Test
    fun samples_are_filtered_correctly() {
        val samples = ArrayList<PitchDifference>()

        samples.add(PitchDifference(Note.createNote("E2"), 2.0))
        samples.add(PitchDifference(Note.createNote("E2"), 2.0))
        samples.add(PitchDifference(Note.createNote("B3"), 3.0))
        samples.add(PitchDifference(Note.createNote("E2"), 2.0))
        samples.add(PitchDifference(Note.createNote("G3"), 4.0))
        samples.add(PitchDifference(Note.createNote("B3"), 3.0))

        val filteredSamples: List<PitchDifference>? = samples.filter { it.closest == Note.createNote("B3") }

        for ((closest) in filteredSamples!!) {
            assertThat(closest.name, `is`("B"))
        }
    }

    @Test
    @Throws(Exception::class)
    fun the_most_frequent_note_is_extracted_correctly() {
        val samples = ArrayList<PitchDifference>()

        samples.add(PitchDifference(Note.createNote("E2"), 2.0))
        samples.add(PitchDifference(Note.createNote("E2"), 2.0))
        samples.add(PitchDifference(Note.createNote("B3"), 3.0))
        samples.add(PitchDifference(Note.createNote("E2"), 2.0))
        samples.add(PitchDifference(Note.createNote("G3"), 4.0))
        samples.add(PitchDifference(Note.createNote("B3"), 3.0))

        val note = Sampler.extractMostFrequentNote(samples)

        assertThat(note!!.name, `is`("E"))
    }

    @Test
    fun if_there_are_notes_with_the_same_number_of_occurrences_one_of_them_is_returned() {
        val samples = ArrayList<PitchDifference>()

        samples.add(PitchDifference(Note.createNote("G3"), 2.0))
        samples.add(PitchDifference(Note.createNote("E2"), 2.0))
        samples.add(PitchDifference(Note.createNote("B3"), 3.0))
        samples.add(PitchDifference(Note.createNote("E2"), 2.0))
        samples.add(PitchDifference(Note.createNote("B3"), 3.0))

        val note = Sampler.extractMostFrequentNote(samples)

        assertThat(note!!.name, either(`is`("E")).or(`is`("B")))
    }
}