package natec.androidapp.mycalc.ui.calculator

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import natec.androidapp.mycalc.databinding.FragmentCalculatorBinding
import natec.androidapp.mycalc.ui.calculator.viewmodel.CalculatorViewModel

class CalculatorFragment : Fragment() {

    //a view's lifecycle in a fragment is different than the fragments lifecycle
    //the fragment can exist while its views don't so we need to set the viewBinding to null
    private var _binding: FragmentCalculatorBinding? = null

    //_binding can be null and we don't want to handle nullability everywhere we use _binding
    private val binding get() = _binding!!

    private lateinit var calculatorViewModel: CalculatorViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        //multiple fragments share the same instance of the calculatorViewModel
        // so scope the viewModel to the activity hosting all of the fragments.
        calculatorViewModel = ViewModelProvider(requireActivity()).get(CalculatorViewModel::class.java)
        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)

        //disable keyboard pop-up
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            binding.textCalcOutput.showSoftInputOnFocus = false
        }else{
            binding.textCalcOutput.setTextIsSelectable(true)
        }

        calculatorViewModel.input.observe(viewLifecycleOwner, {
            binding.textCalcOutput.setText(it)
            if(it.isNotEmpty()){
                binding.textCalcOutput.setSelection(it.length)
            }
        })

        calculatorViewModel.preview.observe(viewLifecycleOwner, {
            binding.textPreview.setText(it)
        })

        return binding.root
    }
}