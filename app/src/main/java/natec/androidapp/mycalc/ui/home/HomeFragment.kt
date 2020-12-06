package natec.androidapp.mycalc.ui.home

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

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var numberAdd = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = view.findViewById(R.id.text_home)

        homeViewModel.liveHomeText.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        initButtons(view)
        return view
    }


    private fun initButtons(view: View){
        val buttonAdd = view.findViewById<Button>(R.id.buttonAdd)
        val buttonSub = view.findViewById<Button>(R.id.buttonSub)
        val buttonChangeText = view.findViewById<Button>(R.id.setText)

        buttonAdd.setOnClickListener { homeViewModel.addOne() }
        buttonSub.setOnClickListener { homeViewModel.subOne() }
        buttonChangeText.setOnClickListener { homeViewModel.changeText() }
    }
}