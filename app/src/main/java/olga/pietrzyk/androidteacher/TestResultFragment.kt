package olga.pietrzyk.androidteacher


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import olga.pietrzyk.androidteacher.databinding.FragmentTestResultBinding

/**
 * A simple [Fragment] subclass.
 */
class TestResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentTestResultBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_test_result, container, false)

        binding.btnTryAgain.setOnClickListener { view:View ->
            Navigation.findNavController(view).navigate(R.id.action_testResultFragment_to_testFragment)
        }
        return binding.root


    }


}
