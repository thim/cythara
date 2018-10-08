package com.github.cythara.presentation.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import com.github.cythara.R
import com.github.cythara.domain.PitchDifference

class GaugeView : View {

    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val colorPrimary: Int
    private val colorSecondary: Int

    init {
        val typedValue = TypedValue()

        colorPrimary = if (!isInEditMode) {
            context.theme.resolveAttribute(R.attr.gaugePrimaryColor, typedValue, true)
            typedValue.data
        } else {
            resources.getColor(R.color.gray)
        }

        colorSecondary = if (!isInEditMode) {
            context.theme.resolveAttribute(R.attr.gaugeSecondaryColor, typedValue, true)
            typedValue.data
        } else {
            resources.getColor(R.color.grayDark)
        }

    }

    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize()
    }

    private fun initialize() {
        arcPaint.style = Paint.Style.STROKE
        arcPaint.strokeWidth = 15f
        if (!isInEditMode) {
            arcPaint.typeface = ResourcesCompat.getFont(context, R.font.ubuntu_light)
        }


    }

    override fun onDraw(canvas: Canvas) {
        val arcCenterX: Float = width / 2f
        val arcCenterY: Float = height * 1f
        val textX = 40f

        // Draw the pointers
        val totalNoOfPointers = 60
        val pointerMaxHeight = 20f
        val pointerMinHeight = 2f
        var startX = arcCenterX - arcCenterY + textX + pointerMaxHeight * 2

        if (startX < 0) {
            startX = 0f
        }

        arcPaint.strokeWidth = 10f
        arcPaint.strokeCap = Paint.Cap.ROUND

        val textSize = resources.getDimensionPixelSize(R.dimen.numbersSmallTextSize)
        arcPaint.textSize = textSize.toFloat()

        val save = canvas.save()
        canvas.rotate(30f, arcCenterX, arcCenterY)

        for (i in 0..totalNoOfPointers) {
            var pointerHeight = pointerMinHeight
            arcPaint.color = colorSecondary

            if (i % 10 == 0) {
                pointerHeight = pointerMaxHeight
                arcPaint.color = colorPrimary
                val s = canvas.save()
                arcPaint.strokeWidth = 1f
                arcPaint.style = Paint.Style.FILL
                canvas.rotate(-90f, startX - textX, arcCenterY)

                val textValue = (i - (totalNoOfPointers / 2)).toString()

                val textMeasure = arcPaint.measureText(textValue) / 2

                canvas.drawText(textValue, startX - textX - textMeasure, arcCenterY, arcPaint)
                arcPaint.strokeWidth = 10f
                arcPaint.style = Paint.Style.STROKE
                canvas.restoreToCount(s)
            }
            canvas.drawLine(startX, arcCenterY, startX - pointerHeight, arcCenterY, arcPaint)

            canvas.rotate(120f / totalNoOfPointers, arcCenterX, arcCenterY)
        }

        canvas.restoreToCount(save)
        arcPaint.color = colorPrimary

        canvas.rotate(90f, arcCenterX, arcCenterY)

        canvas.rotate(currentPointer * 2, arcCenterX, arcCenterY)

        canvas.drawLine(startX + 20, arcCenterY, arcCenterX - 10f, arcCenterY, arcPaint)


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        val width = measuredWidth
        val height = measuredHeight

        val widthWithoutPadding = width - paddingLeft - paddingRight
        val heightWithoutPadding = height - paddingTop - paddingBottom

        // set the dimensions
        val size = if (widthWithoutPadding > heightWithoutPadding) {
            heightWithoutPadding
        } else {
            widthWithoutPadding
        }

        setMeasuredDimension(size + paddingLeft + paddingRight, size / 2 + paddingTop + paddingBottom)
    }


    fun setPitchDifference(pitchDifference: PitchDifference) {
        Log.d("TestView", "set: ${pitchDifference.deviation.toFloat()}")

        val start = this.pitchDifference?.deviation?.toFloat() ?: 0f

        this.pitchDifference = pitchDifference

        val end = pitchDifference.deviation.toFloat()

        positionAnimator?.cancel()
        positionAnimator = ValueAnimator.ofFloat(start, end).apply {
            interpolator = LinearInterpolator()
            duration = 200
            addUpdateListener {
                currentPointer = it.animatedValue as Float
                if (currentPointer > 30f) {
                    currentPointer = 32f
                }
                invalidate()
            }
            start()
        }

    }

    private var positionAnimator: ValueAnimator? = null
    private var currentPointer: Float = 0f
    private var pitchDifference: PitchDifference? = null
}