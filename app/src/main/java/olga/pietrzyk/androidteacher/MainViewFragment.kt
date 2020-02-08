package olga.pietrzyk.androidteacher


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import olga.pietrzyk.androidteacher.databinding.FragmentMainViewBinding
import olga.pietrzyk.androidteacher.databinding.FragmentTitleBinding

/**
 * A simple [Fragment] subclass.
 */
class MainViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding=DataBindingUtil.inflate<FragmentMainViewBinding>(inflater, R.layout.fragment_main_view, container, false)

            binding.listOptions?.setOnItemClickListener{parent, view, position, id->
                Log.i("mojeee",position.toString())
                if(position==1) {
                    Log.i("mojeee","jestem tu ale sobie nie działam")
                    view.findNavController().navigate(R.id.action_mainViewFragment_to_testFragment)


            }

            }
        return binding.root


    }

}
