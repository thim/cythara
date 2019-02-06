package com.github.cythara.presentation.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import com.github.cythara.R
import com.github.cythara.domain.PitchDifference
import com.github.cythara.domain.noteName
import java.text.DecimalFormat
import java.util.*

class TunerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {
    private val gaugeView: GaugeView
    private val note: TextView
    private val noteOctave: TextView
    private val reference: TextView
    private val difference: TextView
    private var useScientificNotation: Boolean = false
    private var referencePitch = 440

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.tuner_view, this, true)

        gaugeView = findViewById(R.id.gauge)

        note = findViewById(R.id.note)
        noteOctave = findViewById(R.id.note_octave)
        reference = findViewById(R.id.reference)
        difference = findViewById(R.id.difference)

        displayReferencePitch()
    }

    override fun invalidate() {
        super.invalidate()
        displayReferencePitch()
    }

    fun setPitchDifference(pitchDifference: PitchDifference?) {
        if (pitchDifference != null) {
            gaugeView.setValue(pitchDifference.deviation.toFloat())

            val octave = getOctave(pitchDifference.closest.octave).toString()
            var noteStr = pitchDifference.closest.name
            if (!useScientificNotation) {
                noteStr = noteName(pitchDifference.closest.name)
            }
            note.text = "$noteStr"
            noteOctave.text = octave
            difference.text = formatter.format(pitchDifference.deviation) + "¢"
        }
        displayReferencePitch()
    }

    fun setUseScientificNotation(useScientificNotation: Boolean) {
        this.useScientificNotation = useScientificNotation
    }

    fun setReferencePitch(referencePitch: Int) {
        this.referencePitch = referencePitch
    }

    private fun displayReferencePitch() {
        var noteStr = "La3 "
        if (useScientificNotation) {
            noteStr = "A4 "
        }

        reference.text = "Ref: " + noteStr + String.format(Locale.ENGLISH, "= %d Hz", referencePitch)
    }

    private fun getOctave(octave: Int): Int {
        return octave
//        if (useScientificNotation) {
//            octave
//        } else {
//            octave - 1
//        }
    }

    companion object {
        private val formatter = DecimalFormat("#0")
    }
}