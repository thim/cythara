package com.github.cythara.data.tuning

import com.github.cythara.domain.Note
import com.github.cythara.domain.Tuning

class GuitarDropCTuning internal constructor() : Tuning {
    override val notes: Array<Note>
        get() = arrayOf(
                Note("C", 2),
                Note("G", 2),
                Note("C", 3),
                Note("F", 3),
                Note("A", 3),
                Note("D", 4)
        )
}
