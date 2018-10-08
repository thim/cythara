package com.github.cythara.presentation.main

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import com.github.cythara.CytharaApplication
import com.github.cythara.R
import com.github.cythara.domain.PitchDifference
import com.github.cythara.presentation.NumberPickerDialog
import com.github.cythara.presentation.view.TunerView
import com.jaredrummler.materialspinner.MaterialSpinner
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter
import com.shawnlin.numberpicker.NumberPicker
import java.util.*

class MainActivity : AppCompatActivity(), MainView, MaterialSpinner.OnItemSelectedListener<Any>, NumberPicker.OnValueChangeListener {

    private val presenter = MainPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycle.addObserver(presenter)

        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.RECORD_AUDIO), RECORD_AUDIO_PERMISSION)
        } else {
            startRecording()
        }

        enableTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.showTuning()
        presenter.showPitchReference()
        presenter.showNotation()

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val myToolbar = findViewById<Toolbar>(R.id.my_toolbar)
        myToolbar.setTitle(R.string.app_name)
        myToolbar.showOverflowMenu()
        setSupportActionBar(myToolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_privacy_policy -> {
                val browserIntent = Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://gstraube.github.io/privacy_policy.html"))
                startActivity(browserIntent)
            }
            R.id.set_notation -> {
                presenter.showNotationDialog()
            }
            R.id.toggle_dark_mode -> {
                presenter.toggleDarkMode()
                recreate()
            }
            R.id.set_reference_pitch -> {
                presenter.showReferenceDialog()
            }
        }

        return false
    }

    override fun updatePitchDifference(pitchDifference: PitchDifference?) {
        val tunerView = this.findViewById<TunerView>(R.id.pitch)
        if (tunerView != null) {
            tunerView.setPitchDifference(pitchDifference)
            tunerView.invalidate()
        }
    }

    override fun updatePitchReference(value: Int) {
        val tunerView = this.findViewById<TunerView>(R.id.pitch)
        if (tunerView != null) {
            tunerView.setReferencePitch(value)
            tunerView.invalidate()
        }
    }

    override fun updateTuning(value: Int) {

        val typedValue = TypedValue()
        theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true)
        val textColor = typedValue.data

        theme.resolveAttribute(R.attr.activityBackground, typedValue, true)
        val bgColor = typedValue.data

        val spinner = findViewById<MaterialSpinner>(R.id.tuning)
        val adapter = MaterialSpinnerAdapter(this,
                Arrays.asList(*resources.getStringArray(R.array.tunings)))

        if (CytharaApplication.instance.darkModeEnabled) {
            spinner.setTextColor(textColor)
            spinner.setBackgroundColor(bgColor)
            spinner.setArrowColor(textColor)
        }

        spinner.setAdapter(adapter)
        spinner.setOnItemSelectedListener(this)
        spinner.selectedIndex = value
    }

    override fun updateRecordState(value: Boolean) {

    }

    override fun updateNotation(value: Boolean) {
        val tunerView = this.findViewById<TunerView>(R.id.pitch)
        if (tunerView != null) {
            tunerView.setUseScientificNotation(value)
            tunerView.invalidate()
        }
    }

    override fun openNotationDialog(value: Int) {

        val builder = AlertDialog.Builder(ContextThemeWrapper(this,
                R.style.AppTheme))
        builder.setTitle(R.string.choose_notation)
        builder.setSingleChoiceItems(R.array.notations, value
        ) { dialog, which ->
            presenter.updateNotation(which)
            dialog.dismiss()
            val tunerView = findViewById<TunerView>(R.id.pitch)
            tunerView.invalidate()
        }
        builder.show()
    }

    override fun openPitchReference(value: Int) {
        val dialog = NumberPickerDialog()
        val bundle = Bundle()
        bundle.putInt("current_value", value)
        dialog.arguments = bundle
        dialog.show(fragmentManager, "number_picker")
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            RECORD_AUDIO_PERMISSION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startRecording()
                } else {
                    val alertDialog = AlertDialog.Builder(this@MainActivity).create()
                    alertDialog.setTitle(R.string.permission_required)
                    alertDialog.setMessage("Microphone permission is required. App will be closed")
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK"
                    ) { dialog, which ->
                        dialog.dismiss()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity()
                        } else {
                            finish()
                        }
                    }
                    alertDialog.show()
                }
            }
        }
    }

    override fun onItemSelected(view: MaterialSpinner, position: Int, id: Long, item: Any) {
        presenter.updateTuning(position)
    }

    override fun onValueChange(picker: NumberPicker, oldValue: Int, newValue: Int) {
        presenter.updatePitchReference(newValue)
    }

    private fun startRecording() {
        presenter.startRecording()

    }

    private fun enableTheme() {
        CytharaApplication.instance.darkModeEnabled = presenter.useDarkMode()

        var mode = AppCompatDelegate.MODE_NIGHT_NO
        if (CytharaApplication.instance.darkModeEnabled) {
            mode = AppCompatDelegate.MODE_NIGHT_YES
        }

        AppCompatDelegate.setDefaultNightMode(mode)
    }

    companion object {
        const val RECORD_AUDIO_PERMISSION = 0
    }
}
