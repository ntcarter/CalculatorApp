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
import natec.androidapp.mycalc.databinding.FragmentCalculatorBinding

class CalculatorFragment : Fragment() {

    private lateinit var calculatorViewModel: CalculatorViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        calculatorViewModel = ViewModelProvider(this).get(CalculatorViewModel::class.java)

      //  val binding = FragmentCalculatorBinding.inflate(layoutInflater)
        val view = inflater.inflate(R.layout.fragment_calculator, container, false)
        val textView: TextView = view.findViewById(R.id.text_calculator)

        calculatorViewModel.liveHomeText.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        initButtons(view)
        return view
    }

    private fun initButtons(view: View){
        val button0 = view.findViewById<Button>(R.id.buttonNum0)
        val button1 = view.findViewById<Button>(R.id.buttonNum1)
        val button2 = view.findViewById<Button>(R.id.buttonNum2)
        val button3 = view.findViewById<Button>(R.id.buttonNum3)
        val button4 = view.findViewById<Button>(R.id.buttonNum4)
        val button5 = view.findViewById<Button>(R.id.buttonNum5)
        val button6 = view.findViewById<Button>(R.id.buttonNum6)
        val button7 = view.findViewById<Button>(R.id.buttonNum7)
        val button8 = view.findViewById<Button>(R.id.buttonNum8)
        val button9 = view.findViewById<Button>(R.id.buttonNum9)
        val buttonNeg = view.findViewById<Button>(R.id.buttonNeg)
        val buttonDecimal = view.findViewById<Button>(R.id.buttonDecimal)
        val buttonDivide = view.findViewById<Button>(R.id.buttonDivide)
        val buttonMultiply = view.findViewById<Button>(R.id.buttonMultiply)
        val buttonSubtract = view.findViewById<Button>(R.id.buttonSub)
        val buttonAdd = view.findViewById<Button>(R.id.buttonAddition)
        val buttonEquals = view.findViewById<Button>(R.id.buttonEquals)
        val buttonPercent = view.findViewById<Button>(R.id.buttonPercent)
        val buttonPar = view.findViewById<Button>(R.id.buttonPar)
        val buttonClear = view.findViewById<Button>(R.id.buttonClear)
        val buttonMemoryClear = view.findViewById<Button>(R.id.buttonMC)
        val buttonMemoryReset = view.findViewById<Button>(R.id.buttonMR)
        val buttonMPlus = view.findViewById<Button>(R.id.buttonMPlus)
        val buttonMMinus = view.findViewById<Button>(R.id.buttonMMinus)
        val buttonOther1 = view.findViewById<Button>(R.id.buttonOther1)
        val buttonOther2 = view.findViewById<Button>(R.id.buttonOther2)
        val buttonDelete = view.findViewById<Button>(R.id.buttonDelete)
    }
}