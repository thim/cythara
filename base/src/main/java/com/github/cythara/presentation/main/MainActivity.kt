package com.github.cythara.presentation.main

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.Toolbar
import android.view.*
import com.github.cythara.CytharaApplication
import com.github.cythara.R
import com.github.cythara.domain.PitchDifference
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_main.*
import java.util.*

class MainActivity : AppCompatActivity(), MainView, OnValueChangeListener {

    private val presenter = MainPresenter(this)

    private lateinit var adapter: TuningAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycle.addObserver(presenter)

        enableTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        val click = View.OnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                bottom_icon.setImageResource(R.drawable.ic_chevron_down)
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                bottom_icon.setImageResource(R.drawable.ic_chevron_up)
            }
        }

        bottom_icon.setOnClickListener(click)
        bottom_title.setOnClickListener(click)


        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.RECORD_AUDIO), RECORD_AUDIO_PERMISSION)
        } else {
            startRecording()
        }

        adapter = TuningAdapter { value, pos ->
            run {
                presenter.updateTuning(pos)
                closeBottomSheet()
            }
        }
        adapter.addItems(Arrays.asList(*resources.getStringArray(R.array.tunings)))
        bottom_recycler.adapter = adapter

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

    override fun updatePitchDifference(value: PitchDifference?) {
        with(tuner_view) {
            setPitchDifference(value)
            invalidate()
        }
    }

    override fun updatePitchReference(value: Int) {
        with(tuner_view) {
            setReferencePitch(value)
            invalidate()
        }
    }

    override fun updateTuning(value: Int, reload: Boolean) {
        bottom_title.text = adapter.getItem(value)
        if (reload) {
            adapter.selection = value
            adapter.notifyDataSetChanged()
        }
    }

    override fun updateRecordState(value: Boolean) {

    }

    override fun updateNotation(value: Boolean) {
        with(tuner_view) {
            setUseScientificNotation(value)
            invalidate()
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
            tuner_view.invalidate()
        }
        builder.show()
    }

    override fun openPitchReference(value: Int) {
        val dialog = NumberPickerDialog()
        val bundle = Bundle()
        bundle.putInt("current_value", value)
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "pitch_dialog")
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            RECORD_AUDIO_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startRecording()
                } else {
                    val alertDialog = AlertDialog.Builder(this@MainActivity).create()
                    alertDialog.setTitle(R.string.permission_title)
                    alertDialog.setMessage(getString(R.string.permission_description))
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

    override fun onValueChange(oldValue: Int, newValue: Int) {
        presenter.updatePitchReference(newValue)
    }

    private fun closeBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottom_icon.setImageResource(R.drawable.ic_chevron_up)
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
