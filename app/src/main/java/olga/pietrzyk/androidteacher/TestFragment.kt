package olga.pietrzyk.androidteacher


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import olga.pietrzyk.androidteacher.databinding.FragmentTitleBinding

//import olga.pietrzyk.androidteacher.databinding.FragmentTestBinding

/**
 * A simple [Fragment] subclass.
 */
class TestFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //val binding: Frag = DataBindingUtil.inflate(inflater, R.layout.fragment_title, container, false)
        //val binding: FragmentTestBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false)
        //val binding = DataBindingUtil.inflate<FragmentTitleBinding>(inflater, R.layout.fragment_test, container, false)
       // return binding.root

        return inflater.inflate(R.layout.fragment_test, container, false)
    }


}
