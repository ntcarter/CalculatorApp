package natec.androidapp.mycalc.ui.calculator.pads

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import natec.androidapp.mycalc.databinding.FragmentNumpadBinding
import natec.androidapp.mycalc.ui.calculator.viewmodel.CalculatorViewModel

private const val TAG = "NumPadFragment"

class NumpadFragment : Fragment() {
    private lateinit var binding: FragmentNumpadBinding
    private lateinit var viewModel: CalculatorViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentNumpadBinding.inflate(inflater, container, false)

        //multiple fragments share the same instance of the calculatorViewModel
        // so scope the viewModel to the activity hosting all of the fragments.
        viewModel = ViewModelProvider(requireActivity()).get(CalculatorViewModel::class.java)

        //initialize calculator buttons
        initButtons()

        return binding.root
    }


    /**
     * initializes and attaches listeners to each of the buttons in this fragment
     */
    private fun initButtons(){
        //Buttons to navigate from this fragment to another one
        binding.btnMainToSpecial.setOnClickListener {
            it.findNavController().navigate(NumpadFragmentDirections.actionNumpadFragmentToSpecialPad())
        }
        binding.btnMainToTrig.setOnClickListener {
            it.findNavController().navigate(NumpadFragmentDirections.actionNumpadFragmentToSecondarySpecialPadFragment())
        }

        //digit buttons
        binding.btnNum0.setOnClickListener { viewModel.addToInput("0") }
        binding.btnNum1.setOnClickListener { viewModel.addToInput("1") }
        binding.btnNum2.setOnClickListener { viewModel.addToInput("2") }
        binding.btnNum3.setOnClickListener { viewModel.addToInput("3") }
        binding.btnNum4.setOnClickListener { viewModel.addToInput("4") }
        binding.btnNum5.setOnClickListener { viewModel.addToInput("5") }
        binding.btnNum6.setOnClickListener { viewModel.addToInput("6") }
        binding.btnNum7.setOnClickListener { viewModel.addToInput("7") }
        binding.btnNum8.setOnClickListener { viewModel.addToInput("8") }
        binding.btnNum9.setOnClickListener { viewModel.addToInput("9") }

        //operations
        binding.btnAddition.setOnClickListener { viewModel.addToInput("+") }
        binding.btnSubtract.setOnClickListener { viewModel.addToInput("-") }
        binding.btnMultiply.setOnClickListener { viewModel.addToInput("*") }
        binding.btnDivide.setOnClickListener { viewModel.addToInput("÷") }
        binding.btnPercent.setOnClickListener { viewModel.addToInput("%") }
        binding.btnDecimal.setOnClickListener { viewModel.addToInput(".") }
        binding.btnNeg.setOnClickListener { viewModel.addToInput("N") }
        binding.btnPar.setOnClickListener { viewModel.addToInput("P") }
        binding.btnClear.setOnClickListener { viewModel.addToInput("C") }
        binding.btnMainDelete.setOnClickListener { viewModel.addToInput("D") }
        binding.btnMainDelete.setOnLongClickListener { viewModel.addToInput("C")
            true}
        binding.btnMainEquals.setOnClickListener { viewModel.evaluate() }
    }
}