package com.github.cythara.data

class PitchAdjuster {
    private val adjustmentFactor: Float

    internal constructor() {
        adjustmentFactor = 1f
    }

    constructor(referencePitch: Float) {
        adjustmentFactor = referencePitch / A440
    }

    fun adjustPitch(pitch: Float): Float {
        return pitch / adjustmentFactor
    }

    companion object {
        private val A440 = 440f
    }
}
