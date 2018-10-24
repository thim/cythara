package com.github.cythara.data.tuning

import com.github.cythara.domain.Note
import com.github.cythara.domain.Tuning

class UkuleleTuning internal constructor() : Tuning {
    override val notes: Array<Note>
        get() = arrayOf(
                Note("G", 4),
                Note("C", 4),
                Note("E", 4),
                Note("A", 4)
        )
}
