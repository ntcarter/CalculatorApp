package natec.androidapp.mycalc.ui.calculator.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udojava.evalex.Expression
import natec.androidapp.mycalc.insert
import natec.androidapp.mycalc.ui.calculator.viewmodelhelpers.CalculatorVMHelper
import java.lang.ArithmeticException
import java.lang.NumberFormatException
import java.util.Stack

/**
 * ViewModel to handle the calculator evaluation
 * Uses a helper class that holds all of the functions to add to or remove from the input string
 *
 * Uses the expression parsing library EvalEx by Udo Klimaschewski:
 * https://github.com/uklimaschewski/EvalEx
 */
private const val TAG = "CalculatorViewModel"

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {

    // live data variable for the display that changes as the user provides input
    private val _input = MutableLiveData<String>()
    val input: LiveData<String>
        get() = _input

    //liveData for the preview evaluation
    private val _preview = MutableLiveData<String>()
    val preview: LiveData<String>
        get() = _preview

    //liveData for radian or degree mode. True for degree, false for radians
    private val _radOrDeg = MutableLiveData<Boolean>()
    val radOrDeg: LiveData<Boolean>
        get() = _radOrDeg

    private val numPadHelper = CalculatorVMHelper()

    // makes sure that _input.value isn't null when the first button is pressed
    init {
        _input.value = ""
        _radOrDeg.value = true
    }

    /**
     * Handles user input
     */
    fun addToInput(userInput: String){
        _input.value = when(userInput){
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" ->
                numPadHelper.addDigit(_input.value!! , userInput)
            "+", "-", "*", "÷", ".", "%", "P", "e", "e^x", "x^3", "x^2", "x^y", "2^x", "10^x", "xN1", "PI", "!" ->
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
     * Function to handle special input. Passes it to the helper
     */
    fun addSpecialToInput(userInput: String){
        _input.value = numPadHelper.handleSpecial(_input.value!! , userInput)
    }


    /**
     * Evaluates the string expression stored in _input
     */
    fun evaluate() {
        try {
            var convertedI = convertInput()
            convertedI = wrapInput(convertedI)
            val expression = Expression(convertedI)

            //extra expression val to delay the liveData update so we can set the cursor to the correct spot first
            val e = expression.eval()

            val eD = e.toDouble()
            if(eD % 10.0 == 0.0 && eD <= 10000000){
                //convert to Int to get rid of E (8E+1 becomes 80)
                val intE = e.toInt().toString()
                numPadHelper.cursorPosition = intE.length
                _input.value = intE
            }else{
                numPadHelper.cursorPosition = e.toString().length
                _input.value = e.toString()
            }
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
            var convertedI = convertInput()
            convertedI = wrapInput(convertedI)
            val expression = Expression(convertedI)
            val e = expression.eval()

            val eD = e.toDouble()
            if(eD % 10.0 == 0.0 && eD <= 10000000){
                //convert to Int to get rid of E (8E+1 becomes 80)
                _preview.value = e.toInt().toString()
            }else{
                _preview.value = e.toString()
            }
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
     * Exposed function so UI can get the cursorPosition without holding a reference to the helper class
     */
    fun getCursorPosition(): Int{
        return numPadHelper.cursorPosition
    }

    /**
     * Exposed function so UI can set the cursorPosition without holding a reference to the helper class
     */
    fun setCursorPosition(position: Int){
        numPadHelper.cursorPosition = position
    }

    /**
     * The evaluation library uses annoying formatting for some input so we change it to make it
     * easier to use for the user. It must be changed back into the form the evaluator is expecting
     */
    private fun convertInput(): String{
        var convertInput = _input.value

        //LOG must become LOG10, LN must become LOG. in this order
        convertInput = convertInput!!.replace("LOG", "LOG10")
        convertInput = convertInput.replace("LN", "LOG")
        convertInput = convertInput.replace("÷", "/")

        //convert factorial
        for(i in _input.value!!.length - 1 downTo 0){
            if(input.value!![i] == '!'){
                convertInput = convertInput!!.replaceRange(i , i+1, ")")
                convertInput = insertFact(i , convertInput)
            }
        }

        convertInput = convertInput!!.replace("π", "PI")

        Log.d(TAG, "convertInput AFTER factorial conversion: $convertInput")
        return convertInput
    }

    private fun insertFact(index: Int, convertInput: String): String{
        val opPos = numPadHelper.firstOpFromCursor(convertInput, index)

        return if(opPos == 0){
            convertInput.insert(opPos , "FACT(")
        }else{
            convertInput.insert(opPos + 1, "FACT(")
        }
    }

    private fun wrapInput(input: String): String{
        var resultInput = input

        //Due to a bug (in the library) we add DEG() if we want the calculation in radians and nothing for degrees
        val degRad = if(radOrDeg.value == true){
            ""
        }else{
            "DEG"
        }

        //tracks the number open par
        var numOpenPar = 0

        //holds a value of the number of open parenthesis at the time of reaching this trig function
        //when numberOpenPar equals the top thing in this stack pop the top off and add a )
        val trigStack = Stack<Int>()

        var isTrigPar = false

        //loop over input looking for a unique letter indicating a TRIG function is being used
        var i = 0
        while(i < resultInput.length){
            if(resultInput[i] == 'I' || resultInput[i] == 'C' || resultInput[i] =='T'){
                isTrigPar = true
            }else if(resultInput[i] == '('){
                numOpenPar++
                if(isTrigPar){
                    trigStack.add(numOpenPar)
                    resultInput = resultInput.insert(i + 1, "$degRad(")
                    isTrigPar = false
                }
            }else if(resultInput[i] == ')'){
                numOpenPar--
                if(trigStack.isNotEmpty() && trigStack.peek() == numOpenPar){
                    resultInput = resultInput.insert(i + 1, ")")
                    numOpenPar--
                }
            }
            i++
        }

        return resultInput
    }

    fun changeMode(){
        _radOrDeg.value = !(radOrDeg.value)!!
        evaluateForPreview()
    }
}