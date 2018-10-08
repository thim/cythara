package com.github.cythara.data

import com.github.cythara.data.tuning.CelloTuning
import com.github.cythara.data.tuning.ChromaticTuning
import com.github.cythara.domain.Tuning

object TuningMapper {
    val list = arrayOf(ChromaticTuning(), CelloTuning())

    fun getTuningFromPosition(position: Int): Tuning {
        return if (position < list.size) {
            list[position]
        } else {
            list[0]
        }
    }
}
