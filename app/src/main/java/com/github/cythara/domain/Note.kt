package com.github.cythara.domain

import kotlin.math.pow

val noteLetters = arrayOf("A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#")

val notesSolfege = arrayOf("La", "La#", "Si", "Do", "Do#", "Re", "Re#", "Mi", "Fa", "Fa#", "Sol", "Sol#")

fun noteFrequency(note: String, scale: Int): Float {
    val aFreq = 13.75f // A with octave = -1
    val index = noteLetters.indexOf(note.toUpperCase())
    val aOctave = if (index >= 3) {
        scale
    } else {
        scale + 1
    }
    return 2f.pow(index / 12f) * aFreq * 2f.pow(aOctave)
}

fun noteName(note: String): String {
    val index = noteLetters.indexOf(note.toUpperCase())
    return if (index >= 0) {
        notesSolfege[index]
    } else {
        "??"
    }
}

private enum class Notes {
    A, A_SHARP, B, C, C_SHARP, D, D_SHARP, E, F, F_SHARP, G, G_SHARP
}

data class Note(val name: String, val frequency: Float, val octave: Int) {
    constructor(name: String, octave: Int) : this(name, noteFrequency(name, octave), octave)

    fun getShortRep(): String = name + Int.toString()

    companion object {
        fun createNote(short: String): Note {
            val name: String = if (short.length == 3) {
                short.substring(0, 1)
            } else {
                short[0].toString()
            }
            val octave = short[short.length - 1].toInt()

            return Note(name, octave)
        }
    }
}

