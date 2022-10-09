package com.ahk.rotarymusicknob

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ahk.rotarymusicknob.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MusicKnobView.RotateListener,
    MusicKnobView.StepChangeListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.knob1.mode = MusicKnobMode.STEPS
        binding.knob2.mode = MusicKnobMode.SMOOTH
        binding.knob3.mode = MusicKnobMode.STEPS

        binding.knob1.stepChangeListener = this
        binding.knob2.rotateListener = this
        binding.knob3.stepChangeListener = this

        mainViewModel.knobValue1.observe(this) {
            Log.i("Knob1", "LiveData : ${getString(it)}")
            binding.knobDisplay1.text = getString(it)
        }

        mainViewModel.knobValue2.observe(this) {
            Log.i("Knob2", "LiveData : $it")
            binding.knobDisplay2.text = it.toString()
        }

        mainViewModel.knobValue3.observe(this) {
            Log.i("Knob3", "LiveData : ${getString(it)}")
            binding.knobDisplay3.text = getString(it)
        }
    }

    override fun onRotate(tagg: String, value: Int) {

        //tagg returns View specific tagg(custom string set in xml file)
        mainViewModel.updateKnob2(value)
    }

    override fun onStep(tagg: String, value: MusicKnobSteps) {

        /*tagg returns View specific tagg(custom string set in xml file)
         *Use tagg to check which view is sending data.
         */
        when(tagg){
            "knob1" -> mainViewModel.updateKnob1(value)
            "knob3" -> mainViewModel.updateKnob3(value)
        }
    }
}