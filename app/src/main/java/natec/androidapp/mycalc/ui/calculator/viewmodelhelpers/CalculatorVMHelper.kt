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

        val charBC = getCharBeforeCursor(resultInput)
        //if the last char before our cursor is a ) we want to add a * when adding a digit
        if (charBC == ')' || charBC == 'π' || charBC == 'e') {
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
        var resultInput = input


        val charBC = getCharBeforeCursor(input)
        val charAC = getCharAfterCursor(input)

        when (strToAdd) {
            "+", "-", "*", "÷", "%" -> {
                //dont let a *, /, % be entered after a ( or as the first character
                if ((strToAdd == "*" || strToAdd == "÷" || strToAdd == "%") && (charBC == '(' || cursorPosition == 0)) {
                    return resultInput
                } else if ((strToAdd == "*" || strToAdd == "÷") && charBC != null && isOperation(charBC)
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
            "e" -> {
                if(cursorPosition > 0 && (resultInput[cursorPosition -1] == ')' ||
                            resultInput[cursorPosition -1] == 'e' || resultInput[cursorPosition -1].isDigit() || resultInput[cursorPosition -1] == 'π')){
                    resultInput = resultInput.insert(cursorPosition, "*")
                    cursorPosition++
                }
                resultInput += strToAdd
                cursorPosition+= strToAdd.length
            }
            "e^x" -> {
                if(cursorPosition > 0 && (resultInput[cursorPosition -1] == ')' ||
                            resultInput[cursorPosition -1] == 'e' || resultInput[cursorPosition -1].isDigit() || resultInput[cursorPosition -1] == 'π')){
                    resultInput = resultInput.insert(cursorPosition, "*")
                    cursorPosition++
                }
                resultInput += "e^("
                numOpenLeftPar++
                cursorPosition += 3
            }
            "x^3" -> {
                if (cursorPosition > 0 && (resultInput[cursorPosition -1].isDigit() || resultInput[cursorPosition -1] == 'e' || resultInput[cursorPosition -1] == 'π')){
                    resultInput += "^(3)"
                    cursorPosition += 4
                }
            }
            "x^2" -> {
                if (cursorPosition > 0 &&  (resultInput[cursorPosition -1].isDigit() || resultInput[cursorPosition -1] == 'e' || resultInput[cursorPosition -1] == 'π')){
                    resultInput += "^(2)"
                    cursorPosition += 4
                }
            }
            "x^y" -> {
                if (cursorPosition > 0 &&  (resultInput[cursorPosition -1].isDigit() || resultInput[cursorPosition -1] == 'e' || resultInput[cursorPosition -1] == 'π')){
                    resultInput += "^("
                    cursorPosition += 2
                }
            }
            "2^x" -> {
                if(cursorPosition > 0 && (resultInput[cursorPosition -1] == ')' ||
                            resultInput[cursorPosition -1] == 'e' || resultInput[cursorPosition -1].isDigit() || resultInput[cursorPosition -1] == 'π' )){
                    resultInput = resultInput.insert(cursorPosition, "*")
                    cursorPosition++
                }
                resultInput += "2^("
                numOpenLeftPar++
                cursorPosition += 3
            }
            "10^x" -> {
                if(cursorPosition > 0 && (resultInput[cursorPosition -1] == ')' ||
                            resultInput[cursorPosition -1] == 'e' || resultInput[cursorPosition -1].isDigit() || resultInput[cursorPosition -1] == 'π' )){
                    resultInput = resultInput.insert(cursorPosition, "*")
                    cursorPosition++
                }
                resultInput += "10^("
                numOpenLeftPar++
                cursorPosition += 4
            }
            "xN1"->{
                if (cursorPosition > 0 &&  (resultInput[cursorPosition -1].isDigit() || resultInput[cursorPosition -1] == 'e' || resultInput[cursorPosition -1] == 'π')){
                    resultInput += "^(-1)"
                    cursorPosition += 5
                }
            }
            "PI" ->{
                if(cursorPosition > 0 && (resultInput[cursorPosition -1] == ')' ||
                            resultInput[cursorPosition -1] == 'e' || resultInput[cursorPosition -1].isDigit() )){
                    resultInput = resultInput.insert(cursorPosition, "*")
                    cursorPosition++
                }
                resultInput += "π"
                cursorPosition++
            }
            "!" ->{
                if(cursorPosition > 0 && (resultInput[cursorPosition - 1].isDigit()  || resultInput[cursorPosition - 1] == 'e' || resultInput[cursorPosition - 1] == 'π')){
                    resultInput += "!"
                    cursorPosition++
                }
            }
        }

        //after an operation input is not a result
        isResult = false
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
                if (charBC != null && charBC.isDigit() || charBC == 'π' || charBC == 'e') {
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
                if (charAC != null && charAC.isDigit() || charBC == 'π' || charBC == 'e') {
                    resInput = resInput.insert(cursorPosition, "*")
                    cursorPosition++
                }
            }
        }

        isResult = false
        return resInput
    }

    private fun isOperation(input: Char): Boolean {
        return input == '*' || input == '+' || input == '-' || input == '÷'
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
                '+', '-', '*', '÷', '%' -> totalOperations--
                '(' -> {
                    //get the number of chars around the cursors position to delete
                    if(cursorPosition > 1 && isSpecialChar(input[cursorPosition - 2])){
                        //get the range of special chars
                        val (lIndex, rIndex) = getSpecialRange(input ,cursorPosition - 1)
                        resultInput = resultInput.subSequence(0, lIndex).toString() + resultInput.subSequence(rIndex + 1, input.length).toString()
                        numOpenLeftPar--
                        cursorPosition = lIndex
                        return resultInput
                    }
                    numOpenLeftPar--
                }
                ')' -> numOpenLeftPar++
                else -> {
                    //All else conditions should be a letter of a special input
                    if (isSpecialChar(input[cursorPosition - 1])){
                        //get the range of special chars
                        val (lIndex, rIndex) = getSpecialRange(input, cursorPosition)
                        resultInput = resultInput.subSequence(0, lIndex).toString() + resultInput.subSequence(rIndex + 1, input.length).toString()
                        numOpenLeftPar--
                        cursorPosition = lIndex
                        return resultInput
                    }
                }
            }

            resultInput = input.subSequence(0, cursorPosition - 1).toString() +
                    input.subSequence(cursorPosition, input.length).toString()
            cursorPosition--
            isResult = false
        }
        return resultInput
    }

    /**
     * returns the number of characters around the cursor that are special. Excludes (
     */
    private fun getSpecialRange(input: String, position: Int): Pair<Int, Int>{
        var leftCursor = position
        var rightCursor = position

        while (leftCursor > 0 && isSpecialChar(input[leftCursor - 1])){
            leftCursor--
        }

        while (rightCursor < input.length && isSpecialChar(input[rightCursor])){
            rightCursor++
        }

        return Pair(leftCursor, rightCursor)
    }

    /**
     * Checks to see if the current char is a Special Character
     */
    private fun isSpecialChar(char: Char): Boolean{
        return char == 'L' || char == 'O' || char == 'G' || char == 'N' || char == 'R' || char == 'A'
                || char == 'D' || char == 'S' || char == 'I' || char == 'C' || char == 'T' || char == 'H' || char == 'Q'
    }

    fun clearInput(): String {
        cursorPosition = 0
        numOpenLeftPar = 0
        isResult = false
        return ""
    }

    fun negate(input: String): String {
        var resultInput = input

        //special condition input is empty
        if (input.isEmpty()) {
            resultInput = "(-"
            numOpenLeftPar++
            cursorPosition += 2
            return resultInput
        } else if (input == "(-") {
            resultInput = ""
            numOpenLeftPar--
            cursorPosition = 0
            return resultInput
        }

        val negatePosition = firstOpFromCursor(input, cursorPosition)

        if(resultInput[negatePosition] == '-' && negatePosition > 1 && resultInput[negatePosition - 1]
                == '(' && isSpecialChar( resultInput[negatePosition - 2])){
            resultInput = resultInput.replaceRange(negatePosition, negatePosition + 1, "")
            numOpenLeftPar--
            cursorPosition -= 1
            return resultInput
        // if the value at negatePosition is a - check the thing to the left.
        // if it is a ( we need to undo a negation
        }else if(resultInput[negatePosition] == '-' && negatePosition > 0 && resultInput[negatePosition - 1] == '('){
            resultInput = resultInput.replaceRange(negatePosition -1, negatePosition + 1, "")
            numOpenLeftPar--
            cursorPosition -= 2
            return resultInput
        // our value is already negative in the format -25 remove the -
        // This can only happen with a result, so index will be 0
        }else if((resultInput[negatePosition] == '-' && negatePosition == 0)){
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

        //if negatePosition is 0 we insert negation before the index, if > 0 after the index
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

    fun firstOpFromCursor(expression: String, startPos: Int): Int{
        //if the expression is length 0 or 1 we return the 0th index
        if(expression.length <= 1){
            return 0
        }

        //all expressions at this point have length >= 2
        for(i in startPos - 1 downTo 0){
            if(i == 0){
                return 0
            }
            if (!(expression[i].isDigit())) {
                //these conditions aren't considered an operation
                if (expression[i] != '.' && expression[i] != 'π' && expression[i] != 'e'
                    && !(expression[i]== '+' && expression[i - 1] == 'E') &&
                    !(expression[i]== '-' && expression[i - 1] == 'E') && expression[i] != 'E') {
                    return i
                }
            }
        }

        return 0
    }

    fun handleSpecial(input: String, specialToAdd: String): String{
        var resultInput = input
        //add special at cursorPosition based on the input
        //increase cursor position based on specialToAdd.length
        //if there's a digit on the left then add a *
        val charBC = getCharBeforeCursor(input)

        if(charBC != null && (charBC.isDigit() || charBC == ')')){
            resultInput = resultInput.insert(cursorPosition, "*")
            cursorPosition++
            isResult = false
        }

        resultInput = resultInput.insert(cursorPosition, "$specialToAdd(")
        cursorPosition += specialToAdd.length + 1
        numOpenLeftPar++

        return resultInput
    }
}