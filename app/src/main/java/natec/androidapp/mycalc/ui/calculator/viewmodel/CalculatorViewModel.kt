package natec.androidapp.mycalc.ui.calculator.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udojava.evalex.Expression
import natec.androidapp.mycalc.ui.calculator.viewmodelhelpers.CalculatorVMHelper
import java.lang.ArithmeticException
import java.lang.NumberFormatException

/**
 * ViewModel to handle the calculator evaluation
 * Uses a helper class that holds all of the functions to add to or remove from the input string
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

    private val numPadHelper = CalculatorVMHelper()

    // makes sure that _input.value isn't null when the first button is pressed
    init {
        _input.value = ""
    }

    /**
     * Handles user input
     */
    fun addToInput(userInput: String){
        _input.value = when(userInput){
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" ->
                numPadHelper.addDigit(_input.value!! , userInput)
            "+", "-", "*", "/", ".", "%", "P" ->
                numPadHelper.addNonDigit(_input.value!!, userInput)
            "D" -> numPadHelper.deleteAtCursor(_input.value!!)
            "C" -> numPadHelper.clearInput()
            "N" -> numPadHelper.negate(_input.value!!)
            else -> ""
        }

        if(_input.value!!.isNotEmpty()){
            evaluateForPreview()
        }else{
            //input is empty so make sure the preview is also empty
            _preview.value = ""
        }
    }


    /**
     * Evaluates the string expression stored in _input
     */
    fun evaluate() {
        try {
            val expression = Expression(_input.value)
            //tmp input to delay the liveData update so we can set the cursor to the correct spot first
            val tmpInput = expression.eval().toString()
            numPadHelper.cursorPosition = tmpInput.length
            _input.value = tmpInput
            //clear the preview
            _preview.value = ""
            numPadHelper.isResult = true
        } catch (e: Expression.ExpressionException) {
            //TODO("Remove toast from view model, change viewmodel extension back")
            Toast.makeText(getApplication(), "Invalid expression", Toast.LENGTH_SHORT).show()
        } catch (e: ArithmeticException) {
            Toast.makeText(getApplication(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        } catch (e: NumberFormatException) {
            Toast.makeText(getApplication(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
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
        } catch (e: ArithmeticException) {
            //Toast.makeText(getApplication(),"Error: ${e.message}", Toast.LENGTH_SHORT).show()
        } catch (e: NumberFormatException) {
            Toast.makeText(getApplication(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            //the last thing input caused an error so delete it
            _input.value = numPadHelper.deleteAtCursor(_input.value!!)
        }
    }

    /**
     * Exposed function so UI can get the cursorPosition without holding a reference to the helper classes
     */
    fun getCursorPosition(): Int{
        return numPadHelper.cursorPosition
    }

    /**
     * Exposed function so UI can set the cursorPosition without holding a reference to the helper classes
     */
    fun setCursorPosition(position: Int){
        numPadHelper.cursorPosition = position
    }
}