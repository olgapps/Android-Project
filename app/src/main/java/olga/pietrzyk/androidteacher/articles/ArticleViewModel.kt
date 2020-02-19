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

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var newArticle = MutableLiveData<Article>()
    private val articles =database.getAllArticles()
   // lateinit var article: Article?

   // val articleString=article.toString()

    val articlesString = Transformations.map(articles) { article ->
        formatArticles(article, application.resources)
    }

    init{
        //onStartArticle()
    }


   /* fun findArticleByID(){
        uiScope.launch {
           article = database.get(1)

        }
    }*/
    fun onStartArticle(){
        uiScope.launch {
           // val nerArticle1 = Article("Android Intro","This is the first article about Android")
            val nerArticle2 = Article(2,"Android Layout","This is the first article about Android Layout")
            val nerArticle3 = Article(3,"Android Main Activity","This is the first article about Main Activity")
            val nerArticle4 = Article(4,"Android Fragments","This is the first article about Fragments")

            //insert(nerArticle1)
            insert(nerArticle2)
            insert(nerArticle3)
            insert(nerArticle4)

        }
    }

    private suspend fun insert(article: Article){
        withContext(Dispatchers.IO){
            database.insert(article)
        }
    }
}