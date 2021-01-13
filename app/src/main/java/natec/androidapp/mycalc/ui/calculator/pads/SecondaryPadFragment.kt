package natec.androidapp.mycalc.ui.calculator.pads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import natec.androidapp.mycalc.R
import natec.androidapp.mycalc.databinding.FragmentSecondaryPadBinding
import natec.androidapp.mycalc.ui.calculator.viewmodel.CalculatorViewModel

class SecondaryPadFragment : Fragment() {

    private lateinit var binding: FragmentSecondaryPadBinding
    private lateinit var viewModel: CalculatorViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSecondaryPadBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(CalculatorViewModel::class.java)

        //updates the text on the radOrDeg button depending on the liveData
        viewModel.radOrDeg.observe(viewLifecycleOwner, {
            if(it == true){
                binding.btnSecondaryRadDeg.text = getString(R.string.Rad)
            }else{
                binding.btnSecondaryRadDeg.text = getString(R.string.Deg)
            }
        })

        initButtons()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initButtons(){
        //navigation buttons
        binding.btnSecondaryToMain.setOnClickListener {
            it.findNavController().navigate(SecondaryPadFragmentDirections.actionSecondarySpecialPadFragmentToNumpadFragment())
        }
        binding.btnSecondaryToSpecial.setOnClickListener {
            it.findNavController().navigate(SecondaryPadFragmentDirections.actionSecondarySpecialPadFragmentToSpecialPad())
        }

        binding.btnSecondaryRadDeg.setOnClickListener { viewModel.changeMode() }
        binding.btnSecondaryEquals.setOnClickListener { viewModel.evaluate() }
        binding.btnSecondaryDelete.setOnClickListener { viewModel.addToInput("D")  }
        binding.btnSecondaryDelete.setOnLongClickListener { viewModel.addToInput("C")
                 true}
        binding.btnLog.setOnClickListener {  viewModel.addSpecialToInput("LOG") }
        binding.btnLn.setOnClickListener { viewModel.addSpecialToInput("LN") }
        binding.btnSin.setOnClickListener { viewModel.addSpecialToInput("SIN") }
        binding.btnCos.setOnClickListener { viewModel.addSpecialToInput("COS") }
        binding.btnTan.setOnClickListener { viewModel.addSpecialToInput("TAN") }
        binding.btnSinh.setOnClickListener { viewModel.addSpecialToInput("SINH") }
        binding.btnCosh.setOnClickListener { viewModel.addSpecialToInput("COSH") }
        binding.btnTanh.setOnClickListener { viewModel.addSpecialToInput("TANH") }
        binding.btnInverseSin.setOnClickListener { viewModel.addSpecialToInput("ASIN") }
        binding.btnInverseCos.setOnClickListener { viewModel.addSpecialToInput("ACOS")}
        binding.btnInverseTan.setOnClickListener { viewModel.addSpecialToInput("ATAN") }
        binding.btnInverseSinh.setOnClickListener { viewModel.addSpecialToInput("ASINH") }
        binding.btnInverseCosh.setOnClickListener { viewModel.addSpecialToInput("ACOSH") }
        binding.btnInverseTanh.setOnClickListener { viewModel.addSpecialToInput("ATANH") }
    }

}