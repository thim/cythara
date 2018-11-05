package com.github.cythara.data

import com.github.cythara.data.tuning.*
import com.github.cythara.domain.Tuning

object TuningMapper {
    val list = arrayOf(
            ChromaticTuning::class.java,

            CelloTuning::class.java,
            ViolinTuning::class.java,
            BassTuning::class.java,
            GuitarTuning::class.java,
            GuitarDropCSharpTuning::class.java,
            GuitarDropCTuning::class.java,
            GuitarDropDTuning::class.java,
            GuitarOpenGTuning::class.java,
            UkuleleTuning::class.java,
            UkuleleDTuning::class.java
            )

    fun getTuningFromPosition(position: Int): Tuning {
        return if (position < list.size) {
            list[position].newInstance()
        } else {
            list[0].newInstance()
        }
    }
}
