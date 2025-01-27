package olga.pietrzyk.androidteacher.androidTest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import olga.pietrzyk.androidteacher.R

import olga.pietrzyk.androidteacher.databinding.FragmentTestWelcomePageBinding

class TestFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentTestWelcomePageBinding>(
            inflater,
            R.layout.fragment_test_welcome_page, container, false
        )
        binding.buttonStartTest.setOnClickListener { view: View ->
            Navigation.findNavController(view)
                .navigate(R.id.action_testFragment_to_testContentFragment)
        }
        return binding.root
    }
}
