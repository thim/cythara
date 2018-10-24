package com.github.cythara.data.tuning

import com.github.cythara.domain.Note
import com.github.cythara.domain.Tuning

class BassTuning internal constructor() : Tuning {
    override val notes: Array<Note>
        get() = arrayOf(
                Note("E", 1),
                Note("A", 1),
                Note("D", 2),
                Note("G", 2)
        )
}
