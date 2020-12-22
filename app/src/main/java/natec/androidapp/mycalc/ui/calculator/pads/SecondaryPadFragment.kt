package natec.androidapp.mycalc.ui.calculator.pads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import natec.androidapp.mycalc.databinding.FragmentSecondaryPadBinding

class SecondaryPadFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentSecondaryPadBinding.inflate(inflater, container, false)

        binding.btnSecondaryToMain.setOnClickListener {
            it.findNavController().navigate(SecondaryPadFragmentDirections.actionSecondarySpecialPadFragmentToNumpadFragment())
        }
        binding.btnSecondaryToSpecial.setOnClickListener {
            it.findNavController().navigate(SecondaryPadFragmentDirections.actionSecondarySpecialPadFragmentToSpecialPad())
        }

        // Inflate the layout for this fragment
        return binding.root
    }

}