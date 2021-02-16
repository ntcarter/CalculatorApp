package natec.androidapp.mycalc

import natec.androidapp.mycalc.ui.calculator.viewmodelhelpers.CalculatorVMHelper
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class CalculatorVMHelperTest {

    private val calcHelper = CalculatorVMHelper()

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun addDigitTest(){
        //adding digits to different cursor positions
        calcHelper.cursorPosition = 2
        assertEquals("994", calcHelper.addDigit("99", "4"))
        calcHelper.cursorPosition = 1
        assertEquals("949", calcHelper.addDigit("99", "4"))
        calcHelper.cursorPosition = 0
        assertEquals("499", calcHelper.addDigit("99", "4"))
        assertEquals(1, calcHelper.cursorPosition)

        //adding after a result
        calcHelper.isResult = true
        assertEquals("45", calcHelper.addDigit("66464", "45"))
        //testing result being reset after a call to add digit where isResult is true
        assertEquals(false, calcHelper.isResult)
        assertEquals(1, calcHelper.cursorPosition)

        //test special conditions like pi e and )
        calcHelper.cursorPosition = 5
        assertEquals("3*(3)*3", calcHelper.addDigit("3*(3)", "3"))
        calcHelper.cursorPosition = 3
        assertEquals("3*π*3", calcHelper.addDigit("3*π", "3"))
        calcHelper.cursorPosition = 3
        assertEquals("3*e*3", calcHelper.addDigit("3*e", "3"))
        assertEquals(5, calcHelper.cursorPosition)
    }

    @Test
    fun clearInputTest(){
        assertEquals("", calcHelper.clearInput())
        assertEquals(false, calcHelper.isResult)
        assertEquals(0, calcHelper.cursorPosition)
    }

    @Test
    fun addNonDigitTest(){
        //basic operation addition
        calcHelper.cursorPosition = 1
        assertEquals("4+", calcHelper.addNonDigit("4", "+"))
        assertEquals(2, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 1
        assertEquals("4-", calcHelper.addNonDigit("4", "-"))
        assertEquals(2, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 1
        assertEquals("4*", calcHelper.addNonDigit("4", "*"))
        assertEquals(2, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 1
        assertEquals("4÷", calcHelper.addNonDigit("4", "÷"))
        assertEquals(2, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 1
        assertEquals("4%", calcHelper.addNonDigit("4", "%"))
        assertEquals(2, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 1

        //conditions where operations shouldn't be added
        assertEquals("", calcHelper.addNonDigit("", "*"))
        assertEquals("", calcHelper.addNonDigit("", "÷"))
        assertEquals("", calcHelper.addNonDigit("", "%"))
        assertEquals("(", calcHelper.addNonDigit("(", "*"))
        assertEquals("(", calcHelper.addNonDigit("(", "÷"))
        assertEquals("(", calcHelper.addNonDigit("(", "%"))
        //plus and minus should be able to be added after (
        calcHelper.cursorPosition = 1
        assertEquals("(+", calcHelper.addNonDigit("(", "+"))
        calcHelper.cursorPosition = 1
        assertEquals("(-", calcHelper.addNonDigit("(", "-"))

        //replace op before cursor
        calcHelper.cursorPosition = 2
        assertEquals("4+", calcHelper.addNonDigit("4*", "+"))
        assertEquals(2, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 2
        assertEquals("4-", calcHelper.addNonDigit("4*", "-"))
        assertEquals(2, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 2
        assertEquals("4*", calcHelper.addNonDigit("4*", "*"))
        assertEquals(2, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 2
        assertEquals("4*", calcHelper.addNonDigit("4+", "*"))
        assertEquals(2, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 2
        assertEquals("4+", calcHelper.addNonDigit("4÷", "+"))
        assertEquals(2, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 2
        assertEquals("4÷", calcHelper.addNonDigit("4*", "÷"))
        assertEquals(2, calcHelper.cursorPosition)

        //replace op after cursor
        calcHelper.cursorPosition = 1
        assertEquals("4+", calcHelper.addNonDigit("4*", "+"))
        assertEquals(1, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 1
        assertEquals("4-", calcHelper.addNonDigit("4*", "-"))
        assertEquals(1, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 1
        assertEquals("4*", calcHelper.addNonDigit("4*", "*"))
        assertEquals(1, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 1
        assertEquals("4*", calcHelper.addNonDigit("4+", "*"))
        assertEquals(1, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 1
        assertEquals("4+", calcHelper.addNonDigit("4÷", "+"))
        assertEquals(1, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 1
        assertEquals("4÷", calcHelper.addNonDigit("4*", "÷"))
        assertEquals(1, calcHelper.cursorPosition)

        //decimal tests
        calcHelper.cursorPosition = 0
        assertEquals("0.", calcHelper.addNonDigit("", "."))
        assertEquals(2, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 2
        assertEquals("1*0.", calcHelper.addNonDigit("1*", "."))
        assertEquals(4, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 1
        assertEquals("1.", calcHelper.addNonDigit("1", "."))
        assertEquals(2, calcHelper.cursorPosition)

        //e test
        calcHelper.cursorPosition = 1
        assertEquals("3*e", calcHelper.addNonDigit("3", "e"))
        assertEquals(3, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 1
        assertEquals("e*e", calcHelper.addNonDigit("e", "e"))
        assertEquals(3, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 1
        assertEquals(")*e", calcHelper.addNonDigit(")", "e"))
        assertEquals(3, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 0
        assertEquals("e", calcHelper.addNonDigit("", "e"))
        assertEquals(1, calcHelper.cursorPosition)

        //
    }

    @Test
    fun deleteAtCursorTest(){
        //cursor at 0
        calcHelper.cursorPosition = 0
        assertEquals("124124", calcHelper.deleteAtCursor("124124"))
        assertEquals(0, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 4
        assertEquals("12424", calcHelper.deleteAtCursor("124124"))
        assertEquals(3, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 1
        assertEquals("3)", calcHelper.deleteAtCursor("COS(3)"))
        assertEquals(0, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 1
        assertEquals("3)", calcHelper.deleteAtCursor("SIN(3)"))
        assertEquals(0, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 1
        assertEquals("3)", calcHelper.deleteAtCursor("ASIN(3)"))
        assertEquals(0, calcHelper.cursorPosition)
        calcHelper.cursorPosition = 1
        assertEquals("3)", calcHelper.deleteAtCursor("ASINH(3)"))
        assertEquals(0, calcHelper.cursorPosition)
    }
}