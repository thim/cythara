package com.github.cythara;

import com.github.cythara.data.PitchAdjuster;
import com.github.cythara.data.tuning.CelloTuning;
import com.github.cythara.data.tuning.ChromaticTuning;
import com.github.cythara.domain.Note;
import com.github.cythara.domain.PitchComparator;
import com.github.cythara.domain.Tuning;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class PitchAdjusterTest {

    @Test
    public void pitch_is_unchanged_for_A440() {
        PitchAdjuster pitchAdjuster = new PitchAdjuster();

        for (Note pitch : new CelloTuning().getNotes()) {
            Assert.assertThat(pitchAdjuster.adjustPitch(pitch.getFrequency()),
                    is(pitch.getFrequency()));
        }
    }

    @Test
    public void pitch_is_adjusted() {
        PitchAdjuster pitchAdjuster = new PitchAdjuster(442f);
        Assert.assertThat(pitchAdjuster.adjustPitch(662.25f), is(659.2534f));

        pitchAdjuster = new PitchAdjuster(434f);
        Assert.assertThat(pitchAdjuster.adjustPitch(172.23f), is(174.61105f));
    }

    @Test
    public void correct_note_is_retrieved_for_adjusted_pitch() {
        PitchAdjuster pitchAdjuster = new PitchAdjuster(446f);
        float adjustedPitch = pitchAdjuster.adjustPitch(198.67f);

        Tuning tuning = new ChromaticTuning();

        Assert.assertThat(PitchComparator.INSTANCE.retrieveNote(tuning,adjustedPitch).getClosest().getName(),
                is("G"));
        Assert.assertThat(PitchComparator.INSTANCE.retrieveNote(tuning,adjustedPitch).getClosest().getOctave(),
                is(3));

        pitchAdjuster = new PitchAdjuster(432f);
        adjustedPitch = pitchAdjuster.adjustPitch(80.91f);


        Assert.assertThat(PitchComparator.INSTANCE.retrieveNote(tuning,adjustedPitch).getClosest().getName(),
                is("E"));
        Assert.assertThat(PitchComparator.INSTANCE.retrieveNote(tuning,adjustedPitch).getClosest().getOctave(),
                is(2));
    }
}
