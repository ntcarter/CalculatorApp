package natec.androidapp.mycalc.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private const val TAG = "HomeViewModel"
class HomeViewModel : ViewModel() {

    private var numberAdd: Int = 0

    //create a mutableLiveData which can be manipulated from this class
    private val homeText = MutableLiveData<String>("This is the Home Fragment button pressed: $numberAdd")
    val liveHomeText: LiveData<String>
        get() = homeText

    fun addOne(){
        numberAdd++
        homeText.value = "This is the Home Fragment button pressed: $numberAdd"
        Log.d(TAG, ".addOne: $numberAdd")
    }

    fun changeText(){
        if(numberAdd % 2 == 0){
            homeText.value = "Even number so the text is changed"
        }
    }

    fun subOne(){
        numberAdd--
        homeText.value = "This is the Home Fragment button pressed: $numberAdd"
    }
}