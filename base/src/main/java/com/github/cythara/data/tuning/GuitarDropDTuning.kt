package com.github.cythara.data.tuning

import com.github.cythara.domain.Note
import com.github.cythara.domain.Tuning

class GuitarDropDTuning internal constructor() : Tuning {
    override val notes: Array<Note>
        get() = arrayOf(
                Note("D", 2),
                Note("A", 2),
                Note("D", 3),
                Note("G", 3),
                Note("B", 3),
                Note("E", 4)
        )
}
