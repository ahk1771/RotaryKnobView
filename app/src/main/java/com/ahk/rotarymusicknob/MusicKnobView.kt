package com.ahk.rotarymusicknob

import android.content.Context
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.os.VibrationEffect
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.view.GestureDetectorCompat

class MusicKnobView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), GestureDetector.OnGestureListener{

    private val gestureDetector: GestureDetectorCompat
    private var maxValue = 99
    private var minValue = 0
    var rotateListener: RotateListener? = null
    var stepChangeListener: StepChangeListener? = null
    var value = 130
    var mode = MusicKnobMode.SMOOTH
    private var knobDrawable: Drawable? = null
    private var divider = 300f / (maxValue - minValue)
    private var knobImageView: ImageView
    private var lastPosition = 0f
    private var tagg = ""

    interface RotateListener {
        fun onRotate(tagg: String, value: Int)
    }

    interface StepChangeListener{
        fun onStep(tagg: String, value: MusicKnobSteps)
    }

    init {
        this.maxValue = maxValue + 1

        LayoutInflater.from(context)
            .inflate(R.layout.rotary_knob_view, this, true)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.RotaryKnobView,
            0,
            0
        ).apply {
            try {
                knobImageView = findViewById(R.id.knobImageView)
                minValue = getInt(R.styleable.RotaryKnobView_minValue, 0)
                maxValue = getInt(R.styleable.RotaryKnobView_maxValue, 100) + 1
                divider = 300f / (maxValue - minValue)
                value = getInt(R.styleable.RotaryKnobView_initialValue, 50)
                knobDrawable = getDrawable(R.styleable.RotaryKnobView_knobDrawable)
                knobImageView.setImageDrawable(knobDrawable)
                tagg = getString(R.styleable.RotaryKnobView_tagg)!!

            } finally {
                recycle()
            }
        }
        gestureDetector = GestureDetectorCompat(context, this)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (gestureDetector.onTouchEvent(event!!))
            true
        else
            super.onTouchEvent(event)
    }

    override fun onDown(e: MotionEvent?): Boolean {
//        if(knobImageView != null)
//            Log.e("Down", tagg)
        return true
    }

    override fun onShowPress(e: MotionEvent?) {}

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {

        val rotationDegrees = calculateAngle(e2!!.x, e2.y)
        // use only -150 to 150 range (knob min/max points)
        if(mode == MusicKnobMode.SMOOTH)
            if (rotationDegrees >= -150 && rotationDegrees <= 150) {
                setKnobPosition(rotationDegrees)

                // Calculate rotary value
                // The range is the 300 degrees between -150 and 150, so we'll add 150 to adjust the
                // range to 0 - 300
                val valueRangeDegrees = rotationDegrees + 150
                value = ((valueRangeDegrees / divider) + minValue).toInt()
                if (rotateListener != null) rotateListener!!.onRotate(tagg, value)
            }

        if(mode == MusicKnobMode.STEPS)
            when(rotationDegrees){

                in -162f..-126f -> {
                    setKnobPosition(-144f)
                    if (stepChangeListener != null) stepChangeListener!!.onStep(tagg, MusicKnobSteps.ZERO)
                }

                in -126f..-90f -> {
                    setKnobPosition(-108f)
                    if (stepChangeListener != null) stepChangeListener!!.onStep(tagg, MusicKnobSteps.ONE)
                }

                in -90f..-54f -> {
                    setKnobPosition(-72f)
                    if (stepChangeListener != null) stepChangeListener!!.onStep(tagg, MusicKnobSteps.TWO)
                }

                in -54f..-18f -> {
                    setKnobPosition(-36f)
                    if (stepChangeListener != null) stepChangeListener!!.onStep(tagg, MusicKnobSteps.THREE)
                }

                in -18f..18f -> {
                    setKnobPosition(0f)
                    if (stepChangeListener != null) stepChangeListener!!.onStep(tagg, MusicKnobSteps.FOUR)
                }

                in 18f..54f -> {
                    setKnobPosition(36f)
                    if (stepChangeListener != null) stepChangeListener!!.onStep(tagg, MusicKnobSteps.FIVE)
                }

                in 54f..90f -> {
                    setKnobPosition(72f)
                    if (stepChangeListener != null) stepChangeListener!!.onStep(tagg, MusicKnobSteps.SIX)
                }

                in 90f..126f -> {
                    setKnobPosition(108f)
                    if (stepChangeListener != null) stepChangeListener!!.onStep(tagg, MusicKnobSteps.SEVEN)
                }

                in 126f..162f -> {
                    setKnobPosition(144f)
                    if (stepChangeListener != null) stepChangeListener!!.onStep(tagg, MusicKnobSteps.EIGHT)
                }

            }

        return true
    }

    override fun onLongPress(e: MotionEvent?) {}

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }

    private fun calculateAngle(x: Float, y: Float): Float {
        val px = (x / width.toFloat()) - 0.5
        val py = ( 1 - y / height.toFloat()) - 0.5
        var angle = -(Math.toDegrees(Math.atan2(py, px)))
            .toFloat() + 90
        if (angle > 180) angle -= 360
        return angle
    }

    private fun setKnobPosition(angle: Float) {
        val matrix = Matrix()
        knobImageView.scaleType = ImageView.ScaleType.MATRIX
        matrix.postRotate(angle, width.toFloat() / 2, height.toFloat() / 2)
        knobImageView.imageMatrix = matrix

        //Haptic Feedback when using steps
        if(mode == MusicKnobMode.STEPS)
            when(angle){

            -144f ->{
                if(angle > lastPosition ||  angle < lastPosition)
                    knobImageView.performHapticFeedback(VibrationEffect.EFFECT_CLICK)
            }
            -108f ->{
                if(angle > lastPosition ||  angle < lastPosition)
                    knobImageView.performHapticFeedback(VibrationEffect.EFFECT_CLICK)
            }
            -72f ->{
                if(angle > lastPosition ||  angle < lastPosition)
                    knobImageView.performHapticFeedback(VibrationEffect.EFFECT_CLICK)
            }
            -36f ->{
                if(angle > lastPosition ||  angle < lastPosition)
                    knobImageView.performHapticFeedback(VibrationEffect.EFFECT_CLICK)
            }
            0f -> {
                if(angle > lastPosition ||  angle < lastPosition)
                    knobImageView.performHapticFeedback(VibrationEffect.EFFECT_CLICK)
            }
            36f ->{
                if(angle > lastPosition ||  angle < lastPosition)
                    knobImageView.performHapticFeedback(VibrationEffect.EFFECT_CLICK)
            }
            72f ->{
                if(angle > lastPosition ||  angle < lastPosition)
                    knobImageView.performHapticFeedback(VibrationEffect.EFFECT_CLICK)
            }
            108f ->{
                if(angle > lastPosition ||  angle < lastPosition)
                    knobImageView.performHapticFeedback(VibrationEffect.EFFECT_CLICK)
            }
            144f ->{
                if(angle > lastPosition ||  angle < lastPosition)
                    knobImageView.performHapticFeedback(VibrationEffect.EFFECT_CLICK)
            }
        }

        lastPosition = angle
    }
}