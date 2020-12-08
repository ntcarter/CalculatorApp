package natec.androidapp.mycalc.ui.convert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import natec.androidapp.mycalc.R
import natec.androidapp.mycalc.databinding.FragmentConvertBinding

class ConvertFragment : Fragment() {

    //a view's lifecycle in a fragment is different than the fragments lifecycle
    //the fragment can exist while its views don't so we need to set the viewbinding to null
    private var _binding: FragmentConvertBinding? = null
    //_binding can be null and we don't want to handle nullability everywhere we use _binding
    private val binding get() = _binding!!

    private lateinit var convertViewModel: ConvertViewModel
    private var numberAdd = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        convertViewModel = ViewModelProvider(this).get(ConvertViewModel::class.java)
        _binding = FragmentConvertBinding.inflate(inflater, container, false)

        convertViewModel.liveHomeText.observe(viewLifecycleOwner, Observer {
            binding.textConvert.text = it
        })

        initButtons(binding.root)
        return binding.root
    }

    private fun initButtons(view: View){
        val buttonAdd = view.findViewById<Button>(R.id.buttonAdd)
        val buttonSub = view.findViewById<Button>(R.id.buttonSub)
        val buttonChangeText = view.findViewById<Button>(R.id.setText)

        buttonAdd.setOnClickListener { convertViewModel.addOne() }
        buttonSub.setOnClickListener { convertViewModel.subOne() }
        buttonChangeText.setOnClickListener { convertViewModel.changeText() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}