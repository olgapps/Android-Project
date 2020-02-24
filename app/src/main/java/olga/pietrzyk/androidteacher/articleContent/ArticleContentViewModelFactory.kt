package olga.pietrzyk.androidteacher.articleContent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import olga.pietrzyk.androidteacher.articles.ArticleViewModel
import olga.pietrzyk.androidteacher.database.ArticleDatabaseDao
import java.lang.IllegalArgumentException


class ArticleContentViewModelFactory(
    private val articleKey: Long,
    private val dataSource: ArticleDatabaseDao) :ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleContentViewModel::class.java))
            {
                return ArticleContentViewModel(articleKey, dataSource) as T
            }
                throw IllegalArgumentException("unknown viewmodel class")
    }
}
