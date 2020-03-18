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

class MainViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding=DataBindingUtil.inflate<FragmentMainViewBinding>(inflater, R.layout.fragment_main_view, container, false)
        val firstItemIndex = 0
        val secondItemIndex = 1
        val thirdItemIndex = 2
        val fourthItemIndex = 3
        val fifthItemIndex = 4

            binding.listOptions?.setOnItemClickListener { parent, view, position, id ->
                if (position == firstItemIndex) {
                    view.findNavController()
                        .navigate(R.id.action_mainViewFragment_to_taskFragment)
                }
                if (position == secondItemIndex) {
                    view.findNavController().navigate(R.id.action_mainViewFragment_to_testFragment)
                }
                if (position == thirdItemIndex) {
                    view.findNavController()
                        .navigate(R.id.action_mainViewFragment_to_indexedCardsFragment)
                }
                if (position == fourthItemIndex) {
                    view.findNavController().navigate(R.id.action_mainViewFragment_to_mainFragment)
                }
                if (position == fifthItemIndex) {
                    view.findNavController().navigate(R.id.action_mainViewFragment_to_cameraFragment)
                }
            }
        return binding.root
    }
}
