package natec.androidapp.mycalc.ui.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {

    //create a mutableLiveData which can be manipulated from this class
    private val calculatorText = MutableLiveData("This is the calculator")
    val liveHomeText: LiveData<String>
        get() = calculatorText


}