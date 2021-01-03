package natec.androidapp.mycalc.ui.calculator.viewmodelhelpers

import android.util.Log
import natec.androidapp.mycalc.insert

private const val TAG = "CalculatorVMHelper"

/**
 * Helper class to hold all of the logic for adding input to the expression string
 * Does not evaluate the expression string
 * Only the CalculatorViewModel should be accessing functions in this class
 */
class CalculatorVMHelper {

    //stores the state of the input. True if its a result of an operation
    var isResult = false

    //var to track num of digits pressed before an operation or evaluation is pressed
    //should not exceed 20
    private var numDigitsInARow = 0

    //tracks the number of operations in one expression. can not exceed 10
    private var totalOperations = 0

    // Tracks the cursor position for inserting and deleting.
    // CHANGE CURSOR POSITION BEFORE UPDATING _input.value, then compensate when accessing _input.value
    var cursorPosition: Int = 0

    //keeps track of the number of open left parentheses. at 0 there are equal left and right parentheses
    private var numOpenLeftPar = 0

    fun addDigit(input: String, strToAdd: String): String {
        var resultInput = input
        // if the value stored in a input is a result of an operation being evaluated and is not an operation
        // we are ready to begin a new operation
        if (isResult) {
            cursorPosition = 0
            isResult = false
            resultInput = ""
        }

        //if the last char before our cursor is a ) we want to add a * when adding a digit
        if (getCharBeforeCursor(resultInput) == ')') {
            resultInput = resultInput.insert(cursorPosition, "*")
            cursorPosition++
        }

        // insert at the cursor position
        resultInput = resultInput.insert(cursorPosition, strToAdd)
        cursorPosition++

        numDigitsInARow++

        return resultInput
    }

    fun addNonDigit(input: String, strToAdd: String): String {
        Log.d(TAG, "addnondigit starts adding $strToAdd")
        var resultInput = input


        val charBC = getCharBeforeCursor(input)
        val charAC = getCharAfterCursor(input)

        when (strToAdd) {
            "+", "-", "*", "/", "%" -> {
                //dont let a *, /, % be entered after a ( or as the first character
                if ((strToAdd == "*" || strToAdd == "/" || strToAdd == "%") && (charBC == '(' || cursorPosition == 0)) {
                    return resultInput
                } else if ((strToAdd == "*" || strToAdd == "/") && charBC != null && isOperation(charBC)
                    && cursorPosition > 1 && resultInput[cursorPosition - 2] == '(') {
                    return resultInput
                }
                if (charBC != null && isOperation(charBC)) {
                    //operation before cursor, replace with this operation
                    resultInput =
                        resultInput.replaceRange(cursorPosition - 1, cursorPosition, strToAdd)

                } else if (charAC != null && isOperation(charAC)) {
                    //operation after cursor, replace with this operation
                    resultInput =
                        resultInput.replaceRange(cursorPosition, cursorPosition + 1, strToAdd)

                } else if (input.isNotEmpty()) {
                    //insert the operation if input is not empty
                    resultInput = resultInput.insert(cursorPosition, strToAdd)
                    cursorPosition++
                }
            }
            "." -> {
                if(input.isEmpty()){
                    resultInput = "0."
                    cursorPosition += 2
                }else if (charBC != null && charBC != '.') {
                    //if a decimal is after an operation insert a "0." instead of a "."
                    if (isOperation(charBC)) {
                        resultInput = resultInput.insert(cursorPosition, "0.")
                        cursorPosition += 2
                    } else {
                        resultInput = resultInput.insert(cursorPosition, ".")
                        cursorPosition++
                    }
                }
            }
            "P" -> {
                resultInput = addParentheses(input, charBC, charAC)
            }
        }

        //after an operation input is not a result
        isResult = false
        Log.d(TAG, "addNonDigit ends returning: $resultInput")
        return resultInput
    }


    /**
     * Adds a left or right parentheses to the input
     */
    private fun addParentheses(input: String, charBC: Char?, charAC: Char?): String {
        var resInput = input

        // special case input is empty
        if (input.isEmpty()) {
            resInput = "("
            numOpenLeftPar++
            cursorPosition++
        } else {
            if (charBC == '(' || numOpenLeftPar == 0) {
                //if the thing before the ( is a digit insert a *
                if (charBC != null && charBC.isDigit()) {
                    resInput = resInput.insert(cursorPosition, "*")
                    cursorPosition++
                }
                resInput = resInput.insert(cursorPosition, "(")
                numOpenLeftPar++
                cursorPosition++
            } else if (numOpenLeftPar > 0) {
                resInput = resInput.insert(cursorPosition, ")")
                cursorPosition++
                numOpenLeftPar--

                // if the thing to the right of the ) is a digit, add a *
                if (charAC != null && charAC.isDigit()) {
                    resInput = resInput.insert(cursorPosition, "*")
                    cursorPosition++
                }
            }
        }

        isResult = false
        return resInput
    }

    private fun isOperation(input: Char): Boolean {
        return input == '*' || input == '+' || input == '-' || input == '/'
    }

    /**
     * Gets the char after the cursor position (to the right of the cursor)
     */
    private fun getCharAfterCursor(input: String): Char? {
        if (input.isEmpty() || input.length == cursorPosition) {
            return null
        }

        return input[cursorPosition]
    }

    /**
     * Gets the char before the cursor position (to the left of the cursor)
     */
    private fun getCharBeforeCursor(input: String): Char? {
        if (input.isEmpty() || cursorPosition == 0) {
            return null
        }

        return input[cursorPosition - 1]
    }

    fun deleteAtCursor(input: String): String {
        if (cursorPosition == 0) {
            return input
        }

        var resultInput = input

        if (input.isNotEmpty()) {
            //change calc state for thing we are going to delete
            when (input[cursorPosition - 1]) {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> numDigitsInARow--
                '+', '-', '*', '/', '%' -> totalOperations--
                '(' -> numOpenLeftPar--
                ')' -> numOpenLeftPar++
            }

            resultInput = input.subSequence(0, cursorPosition - 1).toString() +
                    input.subSequence(cursorPosition, input.length).toString()
            cursorPosition--
            isResult = false
        }
        return resultInput
    }

    fun clearInput(): String {
        cursorPosition = 0
        numOpenLeftPar = 0
        isResult = false
        return ""
    }

    fun negate(input: String): String{
        var resultInput = input

        //special condition input is empty
        if(input.isEmpty()){
            resultInput = "(-"
            numOpenLeftPar++
            cursorPosition += 2
            return resultInput
        }else if (input == "(-"){
            resultInput = ""
            numOpenLeftPar--
            cursorPosition = 0
            return resultInput
        }

        val negatePosition = firstOpFromCursor(input)

        // if the value at negatePosition is a - check the thing to the left.
        // if it is a ( we need to undo a negation
        if(resultInput[negatePosition] == '-' && negatePosition > 0 && resultInput[negatePosition - 1] == '('){
            resultInput = resultInput.replaceRange(negatePosition -1, negatePosition + 1, "")
            numOpenLeftPar--
            cursorPosition -= 2
            return resultInput
        // our value is already negative in the format -25 remove the -
        // This can only happen with a result, so index will be 0
        }else if(resultInput[negatePosition] == '-' && negatePosition == 0){
            resultInput = resultInput.replaceRange(negatePosition, negatePosition+1, "")
            cursorPosition--
            return resultInput
        }

        //replace a ( with (-
        if(resultInput[negatePosition] == '('){
            resultInput = resultInput.replaceRange(negatePosition, negatePosition + 1, "(-")
            cursorPosition++
            return resultInput
        }

        //if index is 0 we insert negation before the index, if > 0 after the index
        if (negatePosition == 0) {
            resultInput = resultInput.insert(negatePosition, "(-")
            cursorPosition += 2
            numOpenLeftPar++
        } else {
            //all other cases index > 0
            resultInput = resultInput.insert(negatePosition + 1, "(-")
            cursorPosition += 2
            numOpenLeftPar++
        }
        return resultInput
    }

    private fun firstOpFromCursor(expression: String): Int{
        //if the expression is length 0 or 1 we return the 0th index
        if(expression.length <= 1){
            return 0
        }

        //all expressions at this point have length >= 2
        for(i in cursorPosition - 1 downTo 0){
            if (!(expression[i].isDigit())) {
                //these conditions aren't considered an operation
                if (expression[i] != '.' && !(expression[i]== '+' && expression[i - 1] == 'E') && !(expression[i]== '-' && expression[i - 1] == 'E') && expression[i] != 'E') {
                    return i
                }
            }
        }

        return 0
    }
}