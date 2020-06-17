package olga.pietrzyk.androidteacher.indexedCards


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import olga.pietrzyk.androidteacher.R
import olga.pietrzyk.androidteacher.databinding.FragmentIndexedCardsBinding

class IndexedCardsFragment : Fragment() {
    private lateinit var viewModel: IndexedCardsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentIndexedCardsBinding>(
            inflater,
            R.layout.fragment_indexed_cards,
            container,
            false
        )

        val application = requireNotNull(this.activity).application
        val viewModelFactory = IndexedCardsViewModelFactory(application)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(IndexedCardsViewModel::class.java)

        viewModel.current_card.observe(viewLifecycleOwner, Observer { newWord ->
            binding.definition.text = newWord.definition
            binding.description.text = newWord.description
        })
        binding.indexedCardsViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.meaning.observe(viewLifecycleOwner, Observer { meaning ->
            if (meaning == false) {
                binding.description.visibility = View.GONE
                binding.checkMeaningButton.visibility = View.VISIBLE
            }
            if (meaning == true) {
                binding.description.visibility = View.VISIBLE
                binding.checkMeaningButton.visibility = View.GONE
            }
        })
        return binding.root
    }
}
