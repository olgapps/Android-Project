package olga.pietrzyk.androidteacher.articles

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.*
import olga.pietrzyk.androidteacher.database.Article
import olga.pietrzyk.androidteacher.database.ArticleDatabaseDao
import olga.pietrzyk.androidteacher.formatArticles

class ArticleViewModel(
   val database: ArticleDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob= Job()
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val _navigateToArticleId=MutableLiveData<Long>()
    val navigateToArticleViewModelId
        get()=_navigateToArticleId

    fun onArticleNavigated(){
        _navigateToArticleId.value=null
    }

    fun onArticleClicked(ID: Long){
        _navigateToArticleId.value=ID
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var newArticle = MutableLiveData<Article>()
   val articles =database.getAllArticles()
   // lateinit var article: Article?

   // val articleString=article.toString()

    val articlesString = Transformations.map(articles) { article ->
        formatArticles(article, application.resources)
    }

    init{
       // onStartArticle()
    }


   /* fun findArticleByID(){
        uiScope.launch {
           article = database.get(1)

        }
    }*/
    fun onStartArticle(){
        uiScope.launch {
           // val nerArticle1 = Article("Android Intro","This is the first article about Android")
            val nerArticle2 = Article(5,"Android Layout","This is the first article about Android Layout", 0)
            //val nerArticle3 = Article(3,"Android Main Activity","This is the first article about Main Activity")
            val nerArticle4 = Article(6,"Android Fragments","This is the first article about Fragments", 1)

            //insert(nerArticle1)
            insert(nerArticle2)
            //insert(nerArticle3)
            insert(nerArticle4)

        }
    }

    private suspend fun insert(article: Article){
        withContext(Dispatchers.IO){
            database.insert(article)
        }
    }
}