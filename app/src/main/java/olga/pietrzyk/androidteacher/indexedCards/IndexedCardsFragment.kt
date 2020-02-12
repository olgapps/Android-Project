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

/**
 * A simple [Fragment] subclass.
 */
class IndexedCardsFragment : Fragment() {

    private lateinit var viewModel: IndexedCardsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentIndexedCardsBinding>(inflater, R.layout.fragment_indexed_cards, container, false)


        viewModel = ViewModelProviders.of(this).get(IndexedCardsViewModel::class.java)

        viewModel.current_card.observe(this, Observer { newWord ->
            binding.txtDefinition.text = newWord.definition
        })

        binding.btnNext.setOnClickListener {
            viewModel.changeTheCard()
        }

        return binding.root

    }



}
