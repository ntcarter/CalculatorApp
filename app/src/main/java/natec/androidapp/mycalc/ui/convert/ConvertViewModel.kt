package natec.androidapp.mycalc.ui.convert

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import natec.androidapp.mycalc.insert
import kotlin.math.log

private const val TAG = "ConvertViewModel"

class ConvertViewModel : ViewModel() {

    private val _topConvert = MutableLiveData<String>()
    val topConvert: LiveData<String>
        get() = _topConvert

    private val _bottomConvert = MutableLiveData<String>()
    val bottomConvert: LiveData<String>
        get() = _bottomConvert

    private val convertHelper = ConvertHelper()

    private var cursorPosition: Int = 0

    private var hasDec = false

    var convertType = "Length"
    var topType = "Kilometer"
    var bottomType = "Meter"

    init {
        _topConvert.value = ""
        _bottomConvert.value = ""
        cursorPosition = _topConvert.value!!.length
    }

    fun beginConvert() {
        if(topConvert.value!!.isNotEmpty() && topConvert.value!! != "."){
            _bottomConvert.value = convertHelper.convert(convertType, _topConvert.value!!.toDouble(), topType, bottomType).toString()
        }else{
            _bottomConvert.value = "0.0"
        }
    }

    fun addToInput(input: String) {
        if(_topConvert.value!!.length < 15) {
            //delay the liveData update to set the cursor to the correct spot
            val topResult = _topConvert.value!!.insert(cursorPosition, input)
            cursorPosition++

            _topConvert.value = topResult
            //convert
            beginConvert()
        }
    }

    fun addDecimal(){
        var result = ""

        if(cursorPosition > 0 && _topConvert.value!!.length < 15 && !hasDec){
            //delay liveData update to set cursor position
            result = _topConvert.value!!.insert(cursorPosition, ".")
            cursorPosition++
            _topConvert.value = result
            hasDec = true
        }else if(_topConvert.value!!.length < 13 && !hasDec){
            //cursorPosition is 0
            //delay liveData update to set cursor position
            result = _topConvert.value!!.insert(cursorPosition, "0.")
            cursorPosition += 2
            _topConvert.value = result
            hasDec = true
        }
    }

    fun delete(){
        if(cursorPosition > 0) {
            if(_topConvert.value!![cursorPosition -1 ] == '.'){
                hasDec = false
            }

            Log.d(TAG, "cursorPosition: $cursorPosition")
            //delay liveData update to set cursor position
            val res = _topConvert.value!!.subSequence(0, cursorPosition - 1).toString() +
                    _topConvert.value!!.subSequence(cursorPosition, _topConvert.value!!.length).toString()
            cursorPosition--
            _topConvert.value = res
        }

        if(_topConvert.value!!.isNotEmpty()){
            beginConvert()
        }else{
            _bottomConvert.value = "0.0"
        }
    }

    fun clear(){
        cursorPosition = 0
        _topConvert.value = ""
        _bottomConvert.value = "0.0"
        hasDec = false
    }

    fun setCursorPos(pos: Int){
        cursorPosition = pos
    }

    fun getCursorPos(): Int {
        return cursorPosition
    }

    fun negate(){
        val topC = _topConvert.value!!.toDouble()

        if(_topConvert.value!!.isNotEmpty() && topConvert.value!! != "."){
            if(topC < 1){
                cursorPosition--
            }else{
                cursorPosition++
            }
            _topConvert.value = (topC * -1).toString()
        }
    }
}