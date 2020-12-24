package natec.androidapp.mycalc.ui.calculator.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udojava.evalex.Expression

/**
 * ViewModel to handle the calculator logic
 * Most functions will manipulate the string _input then using an expression parsing library the
 * string will be evaluated.
 *
 * Uses the expression parsing library EvalEx by Udo Klimaschewski:
 * https://github.com/uklimaschewski/EvalEx
 */
private const val TAG = "CalculatorViewModel"

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {

    // need live data variable for the display that changes as the user provides input
    private val _input = MutableLiveData<String>()
    val input: LiveData<String>
        get() = _input

    // true if the last pressed button was an operation or digit.
    // Starts with true so an operation cant be the first thing input
    private var wasLastPressedOp = true

    //stores the state of the input if its a result of an operation its true
    private var isResult = false

    // makes sure that _input.value isn't null when the first button is pressed
    init {
        _input.value = ""
    }

    /**
     * Adds the user input digit to the evaluation string _input
     */
    fun addDigitToInput(input: String) {
        // if the value stored in a input is a result of an operation being evaluated and is not an operation
        // we are ready to begin a new operation
        if (isResult) {
            _input.value = ""
            isResult = false
        }
        _input.value = _input.value + input
        wasLastPressedOp = false
    }

    fun addOperationToInput(input: String) {
        if (!(wasLastPressedOp)) {
            _input.value = _input.value + input
        }
        wasLastPressedOp = true
        isResult = false
    }

    /**
     * clears all of the user input
     */
    fun clearInput() {
        _input.value = ""
    }

    /**
     * Searches from the end of _input finding the most recent non-digit character and
     * negates that value Ex) 5+5 becomes 5+(-5
     */
    fun negateRecentNumber() {
        TODO()
    }

    fun evaluate() {
        try {
            val expression = Expression(_input.value)
            _input.value = expression.eval().toString()
            isResult = true
        } catch (e: Expression.ExpressionException) {
            Toast.makeText(getApplication(), "Invalid expression", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteLast() {
        if(_input.value!!.isNotEmpty()){
            _input.value = _input.value!!.subSequence(0, _input.value!!.length - 1 ).toString()
        }
    }

}