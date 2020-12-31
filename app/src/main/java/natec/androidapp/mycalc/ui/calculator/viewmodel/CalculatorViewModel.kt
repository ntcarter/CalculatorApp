package natec.androidapp.mycalc.ui.calculator.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udojava.evalex.Expression
import natec.androidapp.mycalc.insert
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

//TODO("move calculator logic to its own class to de-clutter viewmodel")
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

    //tracks the number of operations in one expression. can not exceed 10
    private var totalOperations = 0

    //keeps track of the number of open left parentheses. at 0 there are equal left and right parentheses
    private var numOpenLeftPar = 0

    // Tracks the cursor position for inserting and deleting.
    // CHANGE CURSOR POSITION BEFORE UPDATING _input.value, then compensate when accessing _input.value
    var cursorPosition: Int = 0

    // makes sure that _input.value isn't null when the first button is pressed
    init {
        _input.value = ""
    }

    /**
     * Adds the user input digit to the evaluation string _input at the cursor's current position
     */
    fun addDigitToInput(input: String) {
        if (numDigitsInARow < 20) {
            // if the value stored in a input is a result of an operation being evaluated and is not an operation
            // we are ready to begin a new operation
            if (isResult) {
                cursorPosition = 0
                _input.value = ""
                isResult = false
            }

            //if the last char of _input is a ) we want to add a * when adding a digit
            if (getCharBeforeCursor() == ')'){
                cursorPosition++
                _input.value = _input.value!!.insert(cursorPosition - 1, "*")
                Log.d(TAG, ") check: $cursorPosition")
            }


            // need to set the cursor position before calling insert
            // due to the liveData observer updating the position of the cursor
            cursorPosition++
            _input.value = _input.value!!.insert(cursorPosition - 1, input)

            isOperationDisable = false
            numDigitsInARow++
            Log.d(TAG, "cursorposition after insert: $cursorPosition")

            //update the preview
            if (_input.value!!.length > 1) {
                evaluateForPreview()
            }
        } else {
            Toast.makeText(getApplication(), "Max 20 digits allowed for a number",
                Toast.LENGTH_SHORT).show()
            //TODO("Move toast out of viewModel to activity")
        }
    }

    /**
     * Adds the user input operation to the evaluation string _input at the cursor's current position
     */
    fun addOperationToInput(input: String) {
        //TODO("account for cursorposition")
        if (totalOperations < 10) {
            if (!(isOperationDisable)) {
                //update cursorPosition before insert due to liveData updating on insert
                cursorPosition++
                _input.value = _input.value!!.insert(cursorPosition -1, input)
                totalOperations++
                isOperationDisable = true
            } else if (isOperationDisable && _input.value!!.isNotEmpty()) {
                //the the operation before the cursor and replace it with this operation
                Log.d(TAG,"cursorposition: $cursorPosition")
                Log.d(TAG,"length: ${_input.value!!.length}")
                _input.value!!.subSequence(0, cursorPosition - 1).toString() +
                        _input.value!!.subSequence(cursorPosition , _input.value!!.length).toString()
                _input.value = _input.value!!.insert(cursorPosition, input)
            }

            isResult = false
            numDigitsInARow = 0
            evaluateForPreview()
        } else {
            Toast.makeText(getApplication(), "Max 10 operations allowed in an expression",
                Toast.LENGTH_SHORT).show()
            //TODO("Move toast out of viewModel to activity")
        }
    }

    /**
     * clears all of the user input
     */
    fun clearInput() {
        _input.value = ""
        // disable operation input after clearing for the first input value
        isOperationDisable = true

        //reset various calculator states
        isResult = false
        _preview.value = ""
        numDigitsInARow = 0
        totalOperations = 0
        numOpenLeftPar = 0
        cursorPosition = 0
    }

    /**
     * Searches from the end of _input finding the most recent non-digit character and
     * negates that value Ex) 5+5 becomes 5+(-5...
     * Negations will always lead with (-
     */
    fun negateRecentNumber() {
        //TODO(instead of starting from the end, start from cursor position)
        //special conditions when the input is empty
        if (_input.value!!.isEmpty()) {
            Log.d(TAG, "empty value")
            _input.value = "(-"
            numOpenLeftPar++
            return
        } else if (_input.value == "(-") {
            _input.value = ""
            numOpenLeftPar--
            return
        }

        val index = findFirstOperation(_input.value!!)

        // if index is a - check thing to the left. If it is a ( we need to negate
        if (_input.value!![index] == '-' && index > 0) {
            if (_input.value!![index - 1] == '(') {
                _input.value = _input.value!!.replaceRange(index - 1, index + 1, "")
                numOpenLeftPar--
                return
            }
         // our value is already negative in the format -25 remove the -
         // This can only happen with a result for this calc implementation so index will be 0
        }else if(_input.value!![index] == '-' && index == 0){
            _input.value = _input.value!!.replaceRange(index, index + 1, "")
            return
        }

        //replace a ( with (-
        if(_input.value!![index] == '('){
            _input.value = _input.value!!.replaceRange(index, index + 1, "(-")
            return
        }

        //if index is 0 we insert negation before the index, if > 0 after the index
        if (index == 0) {
            _input.value = _input.value!!.insert(index, "(-")
            numOpenLeftPar++
        } else {
            //all other cases index > 0
            _input.value = _input.value!!.insert(index + 1, "(-")
            numOpenLeftPar++
        }
    }

    /**
     * Finds and returns the index of the first operation from the end of the string if it exists
     * this function is used for finding an index to insert at for negation
     */
    private fun findFirstOperation(expression: String): Int {
        //if expression is 0 or 1 character we return the 0th index
        if (expression.length <= 1) {
            return 0
        }

        //all expressions in this loop have length >= 2
        for (i in expression.length - 1 downTo 0) {
            if (!(expression[i].isDigit())) {
                //these conditions aren't considered an operation
                if (expression[i] != '.' && !(expression[i]== '+' && expression[i - 1] == 'E') && expression[i] != 'E') {
                    return i
                }
            }
        }
        return 0
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
            numOpenLeftPar = 0
            cursorPosition = 0
            //clear the preview
            _preview.value = ""
        } catch (e: Expression.ExpressionException) {
            //TODO("Remove toast from view model, change viewmodel extension back")
            Toast.makeText(getApplication(), "Invalid expression", Toast.LENGTH_SHORT).show()
        } catch (e: ArithmeticException) {
            Toast.makeText(getApplication(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        } catch (e: NumberFormatException) {
            Toast.makeText(getApplication(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            deleteAtCursor()
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
            isOperationDisable = false
            deleteAtCursor()
        }
    }

    /**
     * adds a left or right Parenthesis
     */
    fun addParentheses() {
        //TODO("account for cursorposition")
        //special cases
        if (_input.value == "") {
            _input.value = "("
            numOpenLeftPar++
            isOperationDisable = false
            Log.d(TAG, "Input Empty adding (")
            return
        }

        //The parenthesis added depends on the last char in _input.value and the value of numOpenLeftPar
        if (_input.value!!.isNotEmpty()) {
            if (getCharBeforeCursor() == '(' || numOpenLeftPar == 0) {
                _input.value = _input.value + "("
                numOpenLeftPar++
            } else if (numOpenLeftPar > 0) {
                _input.value = _input.value + ")"
                numOpenLeftPar--
            }
        }

        //if we type a parenthesis the input should not be treated as a result
        if (isResult) {
            isResult = false
        }

        if (numOpenLeftPar == 0) {
            evaluateForPreview()
        }
    }

    /**
     * Removes character at the cursors position
     */
    fun deleteAtCursor() {
        if(cursorPosition == 0){
            return
        }

        if (_input.value!!.isNotEmpty()){
            //change calc state for thing we are going to delete
            when (_input.value!![cursorPosition - 1]) {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> numDigitsInARow--
                '+', '-', '*', '/' -> totalOperations--
                '(' -> numOpenLeftPar--
                ')' -> numOpenLeftPar++
            }

            // decrement cursorposition before changing the liveData
            cursorPosition--
            //removes the thing before the cursor from the input
            _input.value = _input.value!!.subSequence(0, cursorPosition).toString() +
                    _input.value!!.subSequence(cursorPosition + 1, _input.value!!.length).toString()

        }

        evaluateForPreview()
    }

    /**
     * helper to get the last char of _input.
     */
    private fun getCharBeforeCursor(): Char?{
        if(_input.value?.length!! <= 1 || cursorPosition == 0){
            return null
        }

        return _input.value!![cursorPosition - 1]
    }
}