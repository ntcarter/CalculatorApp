package natec.androidapp.mycalc.ui.convert

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private const val TAG = "HomeViewModel"
class ConvertViewModel : ViewModel() {

    private var numberAdd: Int = 0

    //create a mutableLiveData which can be manipulated from this class
    private val convertText = MutableLiveData("This is the Home Fragment button pressed: $numberAdd")
    val liveHomeText: LiveData<String>
        get() = convertText

    fun addOne(){
        numberAdd++
        convertText.value = "This is the Home Fragment button pressed: $numberAdd"
        Log.d(TAG, ".addOne: $numberAdd")
    }

    fun changeText(){
        if(numberAdd % 2 == 0){
            convertText.value = "Even number so the text is changed"
        }
    }

    fun subOne(){
        numberAdd--
        convertText.value = "This is the Home Fragment button pressed: $numberAdd"
    }
}