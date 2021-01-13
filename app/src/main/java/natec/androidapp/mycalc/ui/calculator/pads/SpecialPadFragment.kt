package natec.androidapp.mycalc.ui.calculator.pads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import natec.androidapp.mycalc.R
import natec.androidapp.mycalc.databinding.FragmentSpecialPadBinding
import natec.androidapp.mycalc.ui.calculator.viewmodel.CalculatorViewModel

class SpecialPadFragment : Fragment(){

    private lateinit var binding: FragmentSpecialPadBinding
    private lateinit var viewModel: CalculatorViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSpecialPadBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(CalculatorViewModel::class.java)

        //updates the text on the radOrDeg button depending on the liveData
        viewModel.radOrDeg.observe(viewLifecycleOwner, {
            if(it == true){
                binding.btnSpecialRadDeg.text = getString(R.string.Rad)
            }else{
                binding.btnSpecialRadDeg.text = getString(R.string.Deg)
            }
        })

        initButtons()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initButtons(){
        //buttons for navigation
        binding.btnSpecialToMain.setOnClickListener {
            it.findNavController().navigate(SpecialPadFragmentDirections.actionSpecialPadToNumpadFragment())
        }
        binding.btnSpecialToTrig.setOnClickListener {
            it.findNavController().navigate(SpecialPadFragmentDirections.actionSpecialPadToSecondarySpecialPadFragment())
        }

        binding.btnSpecialRadDeg.setOnClickListener { viewModel.changeMode() }
        binding.btnE.setOnClickListener { viewModel.addToInput("e") }
        binding.btnSpecialEquals.setOnClickListener { viewModel.evaluate() }
        binding.btnSpecialDelete.setOnClickListener { viewModel.addToInput("D") }
        binding.btnSpecialDelete.setOnLongClickListener { viewModel.addToInput("C")
            true }
        binding.btnEPowerX.setOnClickListener { viewModel.addToInput("e^x") }
        binding.btnXPower3.setOnClickListener { viewModel.addToInput("x^3")  }
        binding.btnXPower2.setOnClickListener { viewModel.addToInput("x^2") }
        binding.btnXPowerY.setOnClickListener { viewModel.addToInput("x^y") }
        binding.btn10PowerX.setOnClickListener { viewModel.addToInput("10^x") }
        binding.btn2PowerX.setOnClickListener { viewModel.addToInput("2^x")  }
        binding.btnXPowerNegOne.setOnClickListener { viewModel.addToInput("xN1")}
        binding.btnSquareRoot.setOnClickListener { viewModel.addSpecialToInput("SQRT") }
        binding.btnFactorial.setOnClickListener { viewModel.addToInput("!")  }
        binding.btnPi.setOnClickListener { viewModel.addToInput("PI") }

    }
}