package com.github.cythara

import com.github.cythara.data.tuning.ChromaticTuning
import com.github.cythara.domain.Note
import com.github.cythara.domain.PitchComparator
import com.github.cythara.domain.PitchDifference
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.closeTo
import org.junit.Assert
import org.junit.Test
import java.util.*

class PitchComparatorTest {

    @Test
    @Throws(Exception::class)
    fun retrieveNote() {
        val expectations = HashMap<Float, PitchDifference>()
        expectations[82.40689f] = PitchDifference(Note.createNote("E2"), 0.0)
        expectations[21f] = PitchDifference(Note.createNote("E2"), 33.149314659801846)
        expectations[500f] = PitchDifference(Note.createNote("B4"), 21.309458914380304)
        val tuning = ChromaticTuning()

        for (pitch in expectations.keys) {
            val (closest, deviation) = PitchComparator.retrieveNote(tuning, pitch)
            val expected = expectations[pitch]!!

            Assert.assertThat(closest.name, `is`(expected.closest.name))
            Assert.assertThat(deviation, closeTo(expected.deviation, 0.001))
        }
    }
}
