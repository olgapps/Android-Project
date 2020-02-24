package olga.pietrzyk.androidteacher.articleContent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import olga.pietrzyk.androidteacher.database.Article
import olga.pietrzyk.androidteacher.database.ArticleDatabaseDao

class ArticleContentViewModel (private val articleKey: Long = 0L, dataSource: ArticleDatabaseDao) : ViewModel(){
    val database = dataSource
    private val viewModelJob = Job()
    private val article = MediatorLiveData<Article>()
    fun getArticle()=article

    init{
        article.addSource(database.getArticleWithId(articleKey), article::setValue)
    }

    private val _navigateToArticle=MutableLiveData<Boolean?>()

    val navigateToArticle: LiveData<Boolean?>
        get()=_navigateToArticle



    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun doneNavigating(){
        _navigateToArticle.value=null
    }

    fun onClose(){
        _navigateToArticle.value=true
    }
}
