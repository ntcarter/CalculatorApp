package natec.androidapp.mycalc.ui.calculator.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel to handle the calculator logic
 */
private const val TAG = "CalculatorViewModel"

class CalculatorViewModel : ViewModel() {

    //need live data variable for the display that changes as the user provides input
    private val _input  = MutableLiveData<String>()
    val input: LiveData<String>
        get() = _input

    fun numberPressed(num: String){
        if(_input.value != null){
            _input.value = _input.value + num
        } else{
            _input.value = num
        }
        Log.d(TAG, "_input val: ${_input.value}")
        Log.d(TAG, "input val: ${input.value}")
    }
}