package com.github.cythara.presentation.main


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.widget.TextView
import com.github.cythara.R

class NumberPickerDialog : DialogFragment() {

    private var valueChangeListener: OnValueChangeListener? = null

    private var startValue = 440
    private var value = 440

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        value = arguments?.getInt("current_value") ?: 440
        startValue = value

        val builder = AlertDialog.Builder(ContextThemeWrapper(activity, R.style.AppTheme))
        builder.setMessage(R.string.choose_a_frequency)
        builder.setPositiveButton("OK") { dialog, which ->
            valueChangeListener?.onValueChange(startValue, value)
        }
        builder.setNegativeButton("CANCEL") { dialog, which -> }

        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_view, null, false)

        val txtValue = view.findViewById<TextView>(R.id.value)
        txtValue.text = "$value Hz"

        view.findViewById<TextView>(R.id.button_positive).setOnClickListener {
            value += 1
            txtValue.text = "$value Hz"
        }

        view.findViewById<TextView>(R.id.button_negative).setOnClickListener {
            value -= 1
            txtValue.text = "$value Hz"
        }

        builder.setView(view)
        return builder.create()
    }

    override fun onPause() {
        super.onPause()
        this.dismiss()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnValueChangeListener) {
            this.valueChangeListener = context
        }
    }

}

interface OnValueChangeListener {
    fun onValueChange(oldValue: Int, newValue: Int)
}
