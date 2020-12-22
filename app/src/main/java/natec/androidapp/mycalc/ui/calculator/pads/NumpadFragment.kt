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

        //this viewmodel needs to be the same instance of the viewmodel that CalculatorFragment uses
        viewModel = ViewModelProvider(this).get(CalculatorViewModel::class.java)

        //initialize calculator buttons
        initButtons()

        return binding.root
    }

    private fun initButtons(){

        //Buttons to navigate from this fragment to another one
        binding.btnMainToSpecial.setOnClickListener {
            it.findNavController().navigate(NumpadFragmentDirections.actionNumpadFragmentToSpecialPad())
        }
        binding.btnMainToSecondary.setOnClickListener {
            it.findNavController().navigate(NumpadFragmentDirections.actionNumpadFragmentToSecondarySpecialPadFragment())
        }
        //number buttons
        binding.btnNum0.setOnClickListener {
            viewModel.numberPressed("0")
        }
        binding.btnNum1.setOnClickListener {
            viewModel.numberPressed("1")
        }
        binding.btnNum2.setOnClickListener {
            viewModel.numberPressed("2")
        }
        binding.btnNum3.setOnClickListener {
            viewModel.numberPressed("3")
        }
        binding.btnNum4.setOnClickListener {
            viewModel.numberPressed("4")
        }
        binding.btnNum5.setOnClickListener {
            viewModel.numberPressed("5")
        }
        binding.btnNum6.setOnClickListener {
            viewModel.numberPressed("6")
        }
        binding.btnNum7.setOnClickListener {
            viewModel.numberPressed("7")
        }
        binding.btnNum8.setOnClickListener {
            viewModel.numberPressed("8")
        }
        binding.btnNum9.setOnClickListener {
            viewModel.numberPressed("9")
        }

        //operations
        binding.btnAddition.setOnClickListener {

        }
        binding.btnNeg.setOnClickListener {

        }
        binding.btnMainEquals.setOnClickListener {

        }
        binding.btnDecimal.setOnClickListener {

        }
        binding.btnSubtract.setOnClickListener {

        }
        binding.btnMultiply.setOnClickListener {

        }
        binding.btnDivide.setOnClickListener {

        }
        binding.btnPar.setOnClickListener {

        }
        binding.btnPercent.setOnClickListener {

        }
        binding.btnClear.setOnClickListener {

        }
        binding.btnMainDelete.setOnClickListener {

        }

        //memory
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