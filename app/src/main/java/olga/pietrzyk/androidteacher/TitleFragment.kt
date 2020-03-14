package olga.pietrzyk.androidteacher


import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.fragment_title.*
import olga.pietrzyk.androidteacher.databinding.FragmentTitleBinding

/**
 * A simple [Fragment] subclass.
 */
class TitleFragment : Fragment() {
    lateinit var languagePreference: LanguagePreference
    val languageList = arrayOf("en","pl")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentTitleBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_title, container, false)


        languagePreference= LanguagePreference(context!!)


            binding.spinnerLanguage.adapter =
                ArrayAdapter(context!!, android.R.layout.simple_list_item_1, languageList)


        val lang: String = languagePreference.getLanguage().toString()
        val index = languageList.indexOf(lang)

        if (index>=0){
            binding.spinnerLanguage.setSelection(index)
        }


        binding.playButton.setOnClickListener { view: View->
            languagePreference.setLanguage(languageList[binding.spinnerLanguage.selectedItemPosition])
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
        return NavigationUI.onNavDestinationSelected(item!!,
            view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }


}
