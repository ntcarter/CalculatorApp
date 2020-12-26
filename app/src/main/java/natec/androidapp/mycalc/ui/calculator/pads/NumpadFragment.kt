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
        //TODO("limit the amount of digits they can put into one group of digits, and the amount of operations that can be chained together.")
        //Buttons to navigate from this fragment to another one
        binding.btnMainToSpecial.setOnClickListener {
            it.findNavController().navigate(NumpadFragmentDirections.actionNumpadFragmentToSpecialPad())
        }
        binding.btnMainToSecondary.setOnClickListener {
            it.findNavController().navigate(NumpadFragmentDirections.actionNumpadFragmentToSecondarySpecialPadFragment())
        }

        //digit buttons
        binding.btnNum0.setOnClickListener {
            viewModel.addDigitToInput("0")
        }
        binding.btnNum1.setOnClickListener {
            viewModel.addDigitToInput("1")
        }
        binding.btnNum2.setOnClickListener {
            viewModel.addDigitToInput("2")
        }
        binding.btnNum3.setOnClickListener {
            viewModel.addDigitToInput("3")
        }
        binding.btnNum4.setOnClickListener {
            viewModel.addDigitToInput("4")
        }
        binding.btnNum5.setOnClickListener {
            viewModel.addDigitToInput("5")
        }
        binding.btnNum6.setOnClickListener {
            viewModel.addDigitToInput("6")
        }
        binding.btnNum7.setOnClickListener {
            viewModel.addDigitToInput("7")
        }
        binding.btnNum8.setOnClickListener {
            viewModel.addDigitToInput("8")
        }
        binding.btnNum9.setOnClickListener {
            viewModel.addDigitToInput("9")
        }

        //operations
        binding.btnAddition.setOnClickListener {
            viewModel.addOperationToInput("+")
        }
        binding.btnNeg.setOnClickListener {
            viewModel.negateRecentNumber()
        }
        binding.btnMainEquals.setOnClickListener {
            viewModel.evaluate()
        }
        binding.btnDecimal.setOnClickListener {
            viewModel.addOperationToInput(".")
        }
        binding.btnSubtract.setOnClickListener {
            viewModel.addOperationToInput("-")
        }
        binding.btnMultiply.setOnClickListener {
            viewModel.addOperationToInput("*")
        }
        binding.btnDivide.setOnClickListener {
            viewModel.addOperationToInput("/")
        }
        binding.btnPar.setOnClickListener {
            //TODO("special case (handle this in the viewmodel: if _input is null add one ( if it isnt add a X(, count all left PAR to match with right PAR")
            //TODO("only certain operations (+ and  -) should be valid after a left parenthesis")
        }
        binding.btnPercent.setOnClickListener {
            viewModel.addOperationToInput("%")
            //TODO("If a digit is pressed after then a X should be added for multiplication")
            //TODO("if this operation is entered after a digit it should eval to digit / 100")
            //TODO("when combod with different operations it does different thinngs")
            //when with X: 19.99x7% = 19.99x0.07
            //when with +: 19.99+7% = 19.99+(19.99*0.07)
        }
        binding.btnClear.setOnClickListener {
            viewModel.clearInput()
        }
        binding.btnMainDelete.setOnClickListener {
            viewModel.deleteLast()
        }

        //memory TODO
        binding.btnMc.setOnClickListener {

        }
        binding.btnMMinus.setOnClickListener {

        }
        binding.btnMPlus.setOnClickListener {

        }
        binding.btnMr.setOnClickListener {

        }

        Log.d(TAG, ".initButtons() done")
    }
}