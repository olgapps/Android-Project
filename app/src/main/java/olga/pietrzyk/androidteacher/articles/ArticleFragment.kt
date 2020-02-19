package olga.pietrzyk.androidteacher.articles


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import olga.pietrzyk.androidteacher.R
import olga.pietrzyk.androidteacher.database.ArticleDatabase
import olga.pietrzyk.androidteacher.databinding.FragmentArticleBinding

/**
 * A simple [Fragment] subclass.
 */
class ArticleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentArticleBinding=DataBindingUtil.inflate(inflater, R.layout.fragment_article, container, false)
        // Inflate the layout for this fragment

       val application = requireNotNull(this.activity).application
        val dataSource = ArticleDatabase.getInstance(application).articleDatabaseDao
        val viewModelFactory = ArticleViewModelFactory(dataSource, application)
        val articleViewModel= ViewModelProviders.of(this, viewModelFactory).get(ArticleViewModel::class.java)
        binding.setLifecycleOwner(this)
        binding.atricleViewModel=articleViewModel
        return binding.root
    }


}
