package natec.androidapp.mycalc.ui.calculator.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udojava.evalex.Expression
import java.lang.ArithmeticException
import java.lang.NumberFormatException

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

    //liveData for the preview evaluation
    private val _preview = MutableLiveData<String>()
    val preview: LiveData<String>
        get() = _preview

    // true if the last pressed button was an operation or digit.
    // Starts with true so an operation cant be the first thing input
    private var isOperationDisable = true

    //stores the state of the input. True if its a result of an operation
    private var isResult = false

    //var to track num of digits pressed before an operation or evaluation is pressed
    //should not exceed 20
    private var numDigitsInARow = 0

    //var to track number of operations in one expression. should not exceed 10
    private var totalOperations = 0

    // makes sure that _input.value isn't null when the first button is pressed
    init {
        _input.value = ""
    }

    /**
     * Adds the user input digit to the evaluation string _input
     */
    fun addDigitToInput(input: String) {
        if (numDigitsInARow < 20) {
            // if the value stored in a input is a result of an operation being evaluated and is not an operation
            // we are ready to begin a new operation
            if (isResult) {
                _input.value = ""
                isResult = false
            }
            _input.value = _input.value + input
            isOperationDisable = false
            numDigitsInARow++

            //update the preview
            if (_input.value!!.length > 1) {
                evaluateForPreview()
            }
        }else{
            Toast.makeText(getApplication(), "Max 20 digits allowed for a number", Toast.LENGTH_SHORT).show()
            //TODO("Move out of viewmodel to activity")
        }
    }

    /**
     * Adds the user input operation to the evaluation string _input
     */
    fun addOperationToInput(input: String) {
        if (totalOperations < 10) {
            if (!(isOperationDisable)) {
                _input.value = _input.value + input
                totalOperations++
                isOperationDisable = true
            } else if (isOperationDisable && _input.value!!.isNotEmpty()) {
                //get the last character which is an operation and replace it with this operation
                _input.value = _input.value!!.substring(0, _input.value!!.length - 1)
                _input.value = _input.value + input
            }
            isResult = false
            numDigitsInARow = 0
            evaluateForPreview()
        } else {
            Toast.makeText(getApplication(), "Max 10 operations allowed in an expression", Toast.LENGTH_SHORT).show()
            //TODO("Move out of viewmodel to activity")
        }
    }

    /**
     * clears all of the user input
     */
    fun clearInput() {
        _input.value = ""
        // disable operation input after clearing for the first input value
        isOperationDisable = true
        _preview.value = ""
        numDigitsInARow = 0
        totalOperations = 0
    }

    /**
     * Searches from the end of _input finding the most recent non-digit character and
     * negates that value Ex) 5+5 becomes 5+(-5...
     */
    fun negateRecentNumber() {
        val input = _input.value

        if (input != null) {
            for(i in input.length -1 downTo 0){
                if (!(input[i].isDigit())){
                    Log.d(TAG, "Value: ${input[i]}")
                }
            }
        }
    }

    /**
     * Evaluates the string expression stored in _input
     */
    fun evaluate() {
        try {
            val expression = Expression(_input.value)
            _input.value = expression.eval().toString()
            isResult = true
            numDigitsInARow = 0
            totalOperations = 0
        } catch (e: Expression.ExpressionException) {
            //TODO("Remove toast from view model, change viewmodel extension back")
            Toast.makeText(getApplication(), "Invalid expression", Toast.LENGTH_SHORT).show()
        }catch (e: ArithmeticException){
            Toast.makeText(getApplication(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }catch(e: NumberFormatException){
            Toast.makeText(getApplication(),"Error: ${e.message}", Toast.LENGTH_SHORT).show()
            deleteLast()
        }
        //clear the preview
        _preview.value = ""
    }

    /**
     * Evaluates the string expression in _input for the preview editText
     */
    private fun evaluateForPreview() {
        try {
            val expression = Expression(_input.value)
            _preview.value = expression.eval().toString()
        } catch (e: Expression.ExpressionException) {
            _preview.value = ""
        }catch (e: ArithmeticException){
           //Toast.makeText(getApplication(),"Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }catch(e: NumberFormatException){
            Toast.makeText(getApplication(),"Error: ${e.message}", Toast.LENGTH_SHORT).show()
            //the last thing input caused an error so delete it
            isOperationDisable = false
            deleteLast()
        }
    }

    /**
     * Removes the last typed character from _input
     */
    fun deleteLast() {
        if (_input.value!!.isNotEmpty()) {
            if(_input.value!![_input.value!!.length -1].isDigit()){
                numDigitsInARow--
            }else{
                totalOperations--
            }
            _input.value = _input.value!!.subSequence(0, _input.value!!.length - 1).toString()
        }
        evaluateForPreview()
    }
}