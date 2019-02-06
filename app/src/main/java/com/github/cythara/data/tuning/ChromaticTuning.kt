package com.github.cythara.data.tuning

import com.github.cythara.domain.Note
import com.github.cythara.domain.Tuning
import com.github.cythara.domain.noteLetters

class ChromaticTuning : Tuning {
    override lateinit var notes: Array<Note>

    init {
        val list = mutableListOf<Note>()
        for (octave in -1..9) {
            for (n in noteLetters) {
                list.add(Note(n, octave))
            }
        }
        notes = list.toTypedArray()
    }

}
