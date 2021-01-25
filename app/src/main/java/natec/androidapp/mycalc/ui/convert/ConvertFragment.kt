package natec.androidapp.mycalc.ui.convert

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import natec.androidapp.mycalc.R
import natec.androidapp.mycalc.databinding.FragmentConvertBinding
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "ConvertFragment"
class ConvertFragment : Fragment(), AdapterView.OnItemSelectedListener {

    //a view's lifecycle in a fragment is different than the fragments lifecycle
    //the fragment can exist while its views don't so we need to set the viewBinding to null
    private var _binding: FragmentConvertBinding? = null

    //_binding can be null and we don't want to handle nullability everywhere we use _binding
    private val binding get() = _binding!!

    private lateinit var convertViewModel: ConvertViewModel

    private lateinit var topAdapter: ArrayAdapter<String>
    private lateinit var bottomAdapter: ArrayAdapter<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        convertViewModel = ViewModelProvider(this).get(ConvertViewModel::class.java)
        _binding = FragmentConvertBinding.inflate(inflater, container, false)

        //disable keyboard pop-up
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            binding.editInputValue.showSoftInputOnFocus = false
        }else{
            binding.editInputValue.setTextIsSelectable(true)
        }

        //initialize array adapters
        ArrayAdapter.createFromResource(requireActivity(), R.array.convert_types, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinTypeSelect.adapter = adapter
        }

        //the arrayAdapter needs to take in an arrayList so we can dynamically change it later
        val topList = ArrayList<String>()
        topAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, topList).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinTop.adapter = topAdapter

        val bottomList = ArrayList<String>()
        bottomAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, bottomList).also{adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinBottom.adapter = bottomAdapter

        convertViewModel.topConvert.observe(viewLifecycleOwner, {
            binding.editInputValue.setText(convertViewModel.topConvert.value.toString())
            binding.editInputValue.setSelection(convertViewModel.getCursorPos())
        })

        convertViewModel.bottomConvert.observe(viewLifecycleOwner, {
            binding.textOutputValue.text = convertViewModel.bottomConvert.value.toString()
        })

        // need to request focus or the first click will be consumed by this call (toFocus)
        // instead of performing the onClick
        binding.editInputValue.requestFocusFromTouch()

        binding.editInputValue.setOnClickListener {
            Log.d(TAG, "TOP START: ${binding.editInputValue.selectionStart}")
            convertViewModel.setCursorPos(binding.editInputValue.selectionStart)
        }

        binding.spinBottom.onItemSelectedListener = this
        binding.spinTop.onItemSelectedListener = this
        binding.spinTypeSelect.onItemSelectedListener = this

        initButtons()
        Log.d(TAG, "onCreate ends")
        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        //the type spinner value has changed we need to change the convert spinners
        //if parent has "Length" at index 0 then we changed convert types and need to change data
        if(parent.getItemAtPosition(0) == "Length"){
            //clear the adapters and add the correct arrays to them
            changeAdapterData(parent.getItemAtPosition(pos).toString())
        }

        when(parent.tag){
            "tag_type" -> convertViewModel.convertType = parent.selectedItem.toString()
            "tag_top" -> convertViewModel.topType = parent.selectedItem.toString()
            "tag_bottom" -> convertViewModel.bottomType = parent.selectedItem.toString()
        }

        convertViewModel.beginConvert()
    }

    /**
     * Changes the data in the adapters when a new convert category is selected (ex. Length -> Area)
     */
    private fun changeAdapterData(convertType: String){
        topAdapter.clear()
        bottomAdapter.clear()
        //figure out which type of conversion options to show
        val type = "type_" + convertType.toLowerCase(Locale.ROOT)

        //get the array data from resources
        val id: Int = resources.getIdentifier(type, "array", requireContext().packageName)
        val strArr = resources.getStringArray(id)

        //add the new data to the adapters
        for(str in strArr){
            topAdapter.add(str)
            bottomAdapter.add(str)
        }
        bottomAdapter.notifyDataSetChanged()
        topAdapter.notifyDataSetChanged()
        binding.spinTop.setSelection(0)
        binding.spinBottom.setSelection(1)

         convertViewModel.topType = binding.spinTop.selectedItem.toString()
         convertViewModel.bottomType = binding.spinBottom.selectedItem.toString()
    }

    private fun initButtons(){
        binding.btnConv0.setOnClickListener { convertViewModel.addToInput("0") }
        binding.btnConv1.setOnClickListener { convertViewModel.addToInput("1") }
        binding.btnConv2.setOnClickListener { convertViewModel.addToInput("2") }
        binding.btnConv3.setOnClickListener { convertViewModel.addToInput("3") }
        binding.btnConv4.setOnClickListener { convertViewModel.addToInput("4") }
        binding.btnConv5.setOnClickListener { convertViewModel.addToInput("5") }
        binding.btnConv6.setOnClickListener { convertViewModel.addToInput("6") }
        binding.btnConv7.setOnClickListener { convertViewModel.addToInput("7") }
        binding.btnConv8.setOnClickListener { convertViewModel.addToInput("8")}
        binding.btnConv9.setOnClickListener { convertViewModel.addToInput("9") }
        binding.btnConvDec.setOnClickListener { convertViewModel.addDecimal() }
        binding.btnConvDel.setOnClickListener { convertViewModel.delete() }
        binding.btnConvClear.setOnClickListener { convertViewModel.clear() }
        binding.btnConvNeg.setOnClickListener { convertViewModel.negate() }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}