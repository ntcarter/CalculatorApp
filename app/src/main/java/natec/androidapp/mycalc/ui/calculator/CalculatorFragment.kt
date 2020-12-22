package natec.androidapp.mycalc.ui.calculator

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
        calculatorViewModel = ViewModelProvider(this).get(CalculatorViewModel::class.java)
        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)

        calculatorViewModel.input.observe(viewLifecycleOwner, {
            binding.textCalcOutput.text = it
        })
        return binding.root
    }
}