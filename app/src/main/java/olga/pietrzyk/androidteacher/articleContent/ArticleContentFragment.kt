package olga.pietrzyk.androidteacher.articleContent


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import olga.pietrzyk.androidteacher.R
import olga.pietrzyk.androidteacher.database.ArticleDatabase
import olga.pietrzyk.androidteacher.databinding.FragmentArticleContentBinding

/**
 * A simple [Fragment] subclass.
 */
class ArticleContentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentArticleContentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_content, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = ArticleContentFragmentArgs.fromBundle(arguments!!)


        //create an instance of the view model factory
        val dataSource = ArticleDatabase.getInstance(application).articleDatabaseDao
        val viewModelFactory = ArticleContentViewModelFactory(arguments.articleKey, dataSource)

        val articleViewModel = ViewModelProviders.of(this, viewModelFactory).get(ArticleContentViewModel::class.java)

        binding.articleViewModel=articleViewModel

        binding.setLifecycleOwner(this)

        articleViewModel.navigateToArticle.observe(this, Observer{
            if(it == true){
                this.findNavController().navigate(ArticleContentFragmentDirections.actionArticleContentFragmentToArticleFragment())
                articleViewModel.doneNavigating()
            }
        })

        return binding.root
    }


}
