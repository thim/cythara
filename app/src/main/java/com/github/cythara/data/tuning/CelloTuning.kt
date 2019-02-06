package com.github.cythara.data.tuning

import com.github.cythara.domain.Note
import com.github.cythara.domain.Tuning

class CelloTuning internal constructor() : Tuning {
    override val notes: Array<Note>
        get() = arrayOf(
                Note("C", 2),
                Note("G", 2),
                Note("D", 3),
                Note("A", 3)
        )
}
