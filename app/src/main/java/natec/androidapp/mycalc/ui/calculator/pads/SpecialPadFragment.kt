package natec.androidapp.mycalc.ui.calculator.pads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import natec.androidapp.mycalc.databinding.FragmentSpecialPadBinding

class SpecialPadFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSpecialPadBinding.inflate(inflater, container, false)

        binding.btnSpecialToMain.setOnClickListener {
                it.findNavController().navigate(SpecialPadFragmentDirections.actionSpecialPadToNumpadFragment())
        }
        binding.btnSpecialToSecondary.setOnClickListener {
            it.findNavController().navigate(SpecialPadFragmentDirections.actionSpecialPadToSecondarySpecialPadFragment())
        }
        // Inflate the layout for this fragment
        return binding.root
    }
}