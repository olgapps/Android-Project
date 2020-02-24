package olga.pietrzyk.androidteacher.articles


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

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

        //tell recyclerView about our adapter
        val adapter = ArticlesAdapter(ArticleListener {
                articleId -> articleViewModel.onArticleClicked(articleId)})

        articleViewModel.navigateToArticleViewModelId.observe(this, Observer { article->
            article?.let{
                this.findNavController().navigate(ArticleFragmentDirections.actionArticleFragmentToArticleContentFragment(article))
                articleViewModel.onArticleNavigated()
            }
        })



        //this will thell the adapter we made to dispaly things on the srcreen
        binding.listArticles.adapter=adapter

        //tell the adapter what data it should be adapting
        articleViewModel.articles.observe(viewLifecycleOwner, Observer{
            it?.let{
                adapter.submitList(it)
            }

        })

        binding.setLifecycleOwner(this)
        binding.atricleViewModel=articleViewModel
        return binding.root
    }


}
