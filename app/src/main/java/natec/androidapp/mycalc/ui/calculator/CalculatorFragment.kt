package natec.androidapp.mycalc.ui.calculator

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

class CalculatorFragment : Fragment() {

    private lateinit var calculatorViewModel: CalculatorViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        calculatorViewModel = ViewModelProvider(this).get(CalculatorViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_calculator, container, false)
        val textView: TextView = view.findViewById(R.id.text_calculator)

        calculatorViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        initButtons(view)
        return view
    }

    private fun initButtons(view: View){
        val button0 = view.findViewById<Button>(R.id.buttonNum0)
        val button1 = view.findViewById<Button>(R.id.buttonNum1)
    }
}