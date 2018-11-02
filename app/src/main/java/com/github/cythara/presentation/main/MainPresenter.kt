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

class MainPresenter internal constructor(private val view: MainView) : LifecycleObserver {
    private var pitchTask: PitchTask? = null
    private val preferences = SharedPref()

    private val pitchAdjuster: PitchAdjuster = PitchAdjuster(preferences.pitchReference.toFloat())

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onStop() {
        Log.d(LOG_TAG, "onStop: ")
        stopRecording()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onPause() {
        Log.d(LOG_TAG, "onPause: ")
        stopRecording()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() {
        Log.d(LOG_TAG, "onResume: ")
        if (pitchTask?.isCancelled == true) {
            startRecording()
        }
    }

    fun startRecording() {
        Log.d(LOG_TAG, "startRecording: ")
        val tuning = TuningMapper.getTuningFromPosition(preferences.currentTuning)
        pitchTask = PitchTask(view, tuning, pitchAdjuster).apply { execute() }
    }

    private fun stopRecording() {
        Log.d(LOG_TAG, "stopRecording: ")
        pitchTask?.let {
            if (!it.isCancelled) {
                it.stopAudioDispatcher()
                it.cancel(true)
            }
        }
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
        pitchAdjuster.setReference(value.toFloat())
        view.updatePitchReference(value)
    }

    fun showPitchReference() {
        view.updatePitchReference(preferences.pitchReference)
    }

    fun updateTuning(value: Int) {
        preferences.currentTuning = value
        view.updateTuning(value, false)
        stopRecording()
        startRecording()
    }

    fun showTuning() {
        view.updateTuning(preferences.currentTuning, true)
    }

    private class PitchTask internal constructor(private val view: MainView?, private val tuning: Tuning, private var pitchAdjuster: PitchAdjuster) : AsyncTask<Void, PitchDifference, Void>() {
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
                    val adjustedPitch = pitchAdjuster.adjustPitch(pitch)
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
            if (pitchDifference.isNotEmpty()) {
                view?.updatePitchDifference(pitchDifference[0])
            } else {
                view?.updatePitchDifference(null)
            }
        }

        internal fun stopAudioDispatcher() {
            audioDispatcher?.let {
                if (!it.isStopped) {
                    it.stop()
                    Log.d(LOG_TAG, "Audio Processor - stopped")
                }
            }
            view?.updateRecordState(false)
        }
    }

    companion object {
        private const val LOG_TAG = "MainPresenter"
        private const val SAMPLE_RATE = 44100
        private const val BUFFER_SIZE = 1024 * 4
        private const val OVERLAP = 768 * 4
        private const val MIN_ITEMS_COUNT = 15
        private val pitchDifferences = arrayListOf<PitchDifference>()
    }

}