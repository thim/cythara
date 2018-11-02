package com.github.cythara;

import com.github.cythara.domain.PitchComparator;
import com.github.cythara.domain.PitchDifference;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.github.cythara.data.tuning.GuitarTuning.Pitch.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;

public class PitchComparatorTest {

    @Test
    public void retrieveNote() throws Exception {
        Map<Float, PitchDifference> expectations = new HashMap<>();
        expectations.put(20f, new PitchDifference(E2, -2451.3202694972874));
        expectations.put(500f, new PitchDifference(E4, 721.3071582323822));
        expectations.put(197.67f, new PitchDifference(G3, 14.705999652460953));
        expectations.put(128.415f, new PitchDifference(D3, -232.0232233030192));

        for (Float pitch : expectations.keySet()) {
            PitchDifference actual = PitchComparator.INSTANCE.retrieveNote(pitch);
            PitchDifference expected = expectations.get(pitch);

            Assert.assertThat(actual.closest, is(expected.closest));
            Assert.assertThat(actual.deviation, closeTo(expected.deviation, 0.001));
        }
    }
}
