package com.ahk.rotarymusicknob

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    var knobValue1 = MutableLiveData<Int>(R.string.FOUR)
    var knobValue2 = MutableLiveData<Int>(0)
    var knobValue3 = MutableLiveData<Int>(R.string.FOUR)

    fun updateKnob1(knobValue: MusicKnobSteps){

        when(knobValue){

            MusicKnobSteps.ZERO ->knobValue1.value = R.string.ZERO

            MusicKnobSteps.ONE ->knobValue1.value = R.string.ONE

            MusicKnobSteps.TWO ->knobValue1.value = R.string.TWO

            MusicKnobSteps.THREE ->knobValue1.value = R.string.THREE

            MusicKnobSteps.FOUR ->knobValue1.value = R.string.FOUR

            MusicKnobSteps.FIVE ->knobValue1.value = R.string.FIVE

            MusicKnobSteps.SIX ->knobValue1.value = R.string.SIX

            MusicKnobSteps.SEVEN ->knobValue1.value = R.string.SEVEN

            MusicKnobSteps.EIGHT ->knobValue1.value = R.string.EIGHT
        }
    }

    fun updateKnob2(knobValue: Int){
        knobValue2.value = knobValue
    }

    fun updateKnob3(knobValue: MusicKnobSteps){
        when(knobValue){

            MusicKnobSteps.ZERO ->knobValue3.value = R.string.ZERO

            MusicKnobSteps.ONE ->knobValue3.value = R.string.ONE

            MusicKnobSteps.TWO ->knobValue3.value = R.string.TWO

            MusicKnobSteps.THREE ->knobValue3.value = R.string.THREE

            MusicKnobSteps.FOUR ->knobValue3.value = R.string.FOUR

            MusicKnobSteps.FIVE ->knobValue3.value = R.string.FIVE

            MusicKnobSteps.SIX ->knobValue3.value = R.string.SIX

            MusicKnobSteps.SEVEN ->knobValue3.value = R.string.SEVEN

            MusicKnobSteps.EIGHT ->knobValue3.value = R.string.EIGHT
        }
    }
}