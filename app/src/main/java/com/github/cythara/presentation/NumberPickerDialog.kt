package com.github.cythara.presentation


import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.ContextThemeWrapper

import com.github.cythara.CytharaApplication
import com.github.cythara.R
import com.shawnlin.numberpicker.NumberPicker

class NumberPickerDialog : DialogFragment() {

    private var valueChangeListener: NumberPicker.OnValueChangeListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val numberPicker = NumberPicker(activity)

        val arguments = arguments
        val currentValue = arguments.getInt("current_value", 440)

        numberPicker.minValue = 400
        numberPicker.maxValue = 500
        numberPicker.value = currentValue

        if (CytharaApplication.instance.darkModeEnabled) {

            val typedValue = TypedValue()
            activity.theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true)
            val color = typedValue.data
            numberPicker.textColor = color
            numberPicker.dividerColor = color
            numberPicker.selectedTextColor = color
        }

        val builder = AlertDialog.Builder(ContextThemeWrapper(activity,
                R.style.AppTheme))
        builder.setMessage(R.string.choose_a_frequency)

        builder.setPositiveButton("OK") { dialog, which ->
            valueChangeListener!!.onValueChange(numberPicker,
                    numberPicker.value, numberPicker.value)
        }

        builder.setNegativeButton("CANCEL") { dialog, which -> }

        builder.setView(numberPicker)
        return builder.create()
    }

    override fun onPause() {
        super.onPause()
        this.dismiss()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is NumberPicker.OnValueChangeListener) {
            this.valueChangeListener = context
        }
    }

}
