package com.github.cythara.data.tuning

import com.github.cythara.domain.Note
import com.github.cythara.domain.Tuning

class UkuleleDTuning internal constructor() : Tuning {
    override val notes: Array<Note>
        get() = arrayOf(
                Note("A", 4),
                Note("D", 4),
                Note("F#", 3),
                Note("B", 4)
        )
}
