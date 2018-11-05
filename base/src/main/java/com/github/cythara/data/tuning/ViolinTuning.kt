package com.github.cythara.data.tuning

import com.github.cythara.domain.Note
import com.github.cythara.domain.Tuning

class ViolinTuning internal constructor() : Tuning {
    override val notes: Array<Note>
        get() = arrayOf(
                Note("G", 3),
                Note("D", 4),
                Note("A", 4),
                Note("E", 5)
        )
}
