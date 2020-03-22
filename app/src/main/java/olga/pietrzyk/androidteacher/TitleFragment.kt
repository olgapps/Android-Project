package olga.pietrzyk.androidteacher

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import olga.pietrzyk.androidteacher.databinding.FragmentTitleBinding

class TitleFragment : Fragment() {
    lateinit var languagePreference: LanguagePreference
    val languageList = arrayOf("en","pl")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val firstIndex = 0

        val binding: FragmentTitleBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_title, container, false)

        languagePreference= LanguagePreference(context!!)

        val spinner = binding.spinnerLanguage
        spinner.adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, languageList)

        val lang: String = languagePreference.getLanguage().toString()
        val index = languageList.indexOf(lang)

        Log.i("LANG", "$index")

        if (index >= firstIndex){
            binding.spinnerLanguage.setSelection(index)
        }

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(context!!,
                    activity!!.getString(R.string.nothing_selected) , Toast.LENGTH_SHORT).show()}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Toast.makeText(context!!,
                    " " + binding.spinnerLanguage.selectedItemPosition+ ""+languageList[position], Toast.LENGTH_SHORT).show()
                languagePreference.setLanguage(languageList[position])
            }
        }

        binding.playButton.setOnClickListener { view: View->
            view.findNavController().navigate(R.id.action_titleFragment_to_mainViewFragment)
        }
        setHasOptionsMenu(true)
        return binding.root
   }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }
}
