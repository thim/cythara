package com.github.cythara.presentation.main

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.os.AsyncTask
import android.util.Log
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.android.AudioDispatcherFactory.fromDefaultMicrophone
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm.FFT_YIN
import com.github.cythara.data.PitchAdjuster
import com.github.cythara.data.TuningMapper
import com.github.cythara.domain.*
import java.util.*

class MainPresenter internal constructor(private val view: MainView) : LifecycleObserver {
    private var pitchTask: PitchTask? = null
    private val preferences = SharedPref()

    init {
        pitchAdjuster = PitchAdjuster(preferences.pitchReference.toFloat())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onStop() {
        Log.d(LOG_TAG, "onStop: ")
        if (pitchTask != null && !pitchTask!!.isCancelled) {
            pitchTask?.stopAudioDispatcher()
            pitchTask?.cancel(true)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onPause() {
        Log.d(LOG_TAG, "onPause: ")
        if (pitchTask != null && !pitchTask!!.isCancelled) {
            pitchTask?.stopAudioDispatcher()
            pitchTask?.cancel(true)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() {
        Log.d(LOG_TAG, "onResume: ")
        if (pitchTask?.isCancelled == true) {
            val tuning = TuningMapper.getTuningFromPosition(preferences.currentTuning)
            pitchTask = PitchTask(view, tuning)
            pitchTask?.execute()
        }
    }

    fun startRecording() {
        Log.d(LOG_TAG, "startRecording: ")
        val tuning = TuningMapper.getTuningFromPosition(preferences.currentTuning)
        pitchTask = PitchTask(view, tuning)
        pitchTask?.execute()
    }

    fun updateNotation(value: Int) {
        preferences.isScientificNotation = value == 0
        view.updateNotation(value == 0)
    }

    fun showNotation() {
        view.updateNotation(preferences.isScientificNotation)
    }

    fun showNotationDialog() {
        val useScientificNotation = preferences.isScientificNotation
        val checkedItem = if (useScientificNotation) 0 else 1
        view.openNotationDialog(checkedItem)
    }

    fun toggleDarkMode() {
        val darkMode = preferences.useDarkMode()
        preferences.setDarkMode(!darkMode)
    }

    fun useDarkMode(): Boolean {
        return preferences.useDarkMode()
    }

    fun showReferenceDialog() {
        val value = preferences.pitchReference
        view.openPitchReference(value)
    }

    fun updatePitchReference(value: Int) {
        preferences.pitchReference = value
        view.updatePitchReference(value)
        pitchAdjuster = PitchAdjuster(value.toFloat())
    }

    fun showPitchReference() {
        view.updatePitchReference(preferences.pitchReference)
    }

    fun updateTuning(value: Int) {
        preferences.currentTuning = value
    }

    fun showTuning() {
        view.updateTuning(preferences.currentTuning)
    }

    private class PitchTask internal constructor(private val view: MainView?, private val tuning: Tuning) : AsyncTask<Void, PitchDifference, Void>() {
        private var audioDispatcher: AudioDispatcher? = null

        override fun onPreExecute() {
            view?.updateRecordState(true)
            view?.updatePitchDifference(null)
        }

        override fun doInBackground(vararg params: Void): Void? {
            val pitchDetectionHandler = PitchDetectionHandler { pitchDetectionResult, audioEvent ->
                if (isCancelled) {
                    stopAudioDispatcher()
                    return@PitchDetectionHandler
                }

                val pitch = pitchDetectionResult.pitch
                if (pitch != -1f) {
                    val adjustedPitch = pitchAdjuster!!.adjustPitch(pitch)
                    val pitchDifference = PitchComparator.retrieveNote(tuning, adjustedPitch)

                    pitchDifferences.add(pitchDifference)

                    if (pitchDifferences.size >= MIN_ITEMS_COUNT) {
                        val average = Sampler.calculateAverageDifference(pitchDifferences)
                        publishProgress(average)
                        pitchDifferences.clear()
                    }
                }
            }

            val pitchProcessor = PitchProcessor(FFT_YIN, SAMPLE_RATE.toFloat(),
                    BUFFER_SIZE, pitchDetectionHandler)

            audioDispatcher = fromDefaultMicrophone(SAMPLE_RATE, BUFFER_SIZE, OVERLAP)
            audioDispatcher?.addAudioProcessor(pitchProcessor)

            Log.d(LOG_TAG, "Audio Processor - started")
            audioDispatcher?.run()

            return null
        }

        override fun onProgressUpdate(vararg pitchDifference: PitchDifference) {
            if (view != null) {
                if (pitchDifference.isNotEmpty()) {
                    view.updatePitchDifference(pitchDifference[0])
                } else {
                    view.updatePitchDifference(null)
                }
            }
        }

        internal fun stopAudioDispatcher() {
            if (audioDispatcher != null && !audioDispatcher!!.isStopped) {
                audioDispatcher!!.stop()
                Log.d(LOG_TAG, "Audio Processor - stopped")
            }
            view!!.updateRecordState(false)
        }
    }

    companion object {

        private val LOG_TAG = "MainPresenter"
        private val SAMPLE_RATE = 44100
        private val BUFFER_SIZE = 1024 * 4
        private val OVERLAP = 768 * 4
        private val MIN_ITEMS_COUNT = 15

        private val pitchDifferences = ArrayList<PitchDifference>()
        private var pitchAdjuster: PitchAdjuster? = null
    }

}