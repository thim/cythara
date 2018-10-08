package com.github.cythara;

import com.github.cythara.data.PitchAdjuster;
import com.github.cythara.data.tuning.GuitarTuning;
import com.github.cythara.domain.PitchComparator;

import org.junit.Assert;
import org.junit.Test;

import static com.github.cythara.domain.NoteName.E;
import static com.github.cythara.domain.NoteName.G;
import static com.github.cythara.data.tuning.GuitarTuning.Pitch;
import static org.hamcrest.Matchers.is;

public class PitchAdjusterTest {

    @Test
    public void pitch_is_unchanged_for_A440() {
        PitchAdjuster pitchAdjuster = new PitchAdjuster();

        for (Pitch pitch : GuitarTuning.Pitch.values()) {
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

        Assert.assertThat(PitchComparator.INSTANCE.retrieveNote(adjustedPitch).closest.getName(),
                is(G));
        Assert.assertThat(PitchComparator.INSTANCE.retrieveNote(adjustedPitch).closest.getSign(),
                is(""));
        Assert.assertThat(PitchComparator.INSTANCE.retrieveNote(adjustedPitch).closest.getOctave(),
                is(3));

        pitchAdjuster = new PitchAdjuster(432f);
        adjustedPitch = pitchAdjuster.adjustPitch(80.91f);


        Assert.assertThat(PitchComparator.INSTANCE.retrieveNote(adjustedPitch).closest.getName(),
                is(E));
        Assert.assertThat(PitchComparator.INSTANCE.retrieveNote(adjustedPitch).closest.getSign(),
                is(""));
        Assert.assertThat(PitchComparator.INSTANCE.retrieveNote(adjustedPitch).closest.getOctave(),
                is(2));
    }
}
