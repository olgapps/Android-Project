package olga.pietrzyk.androidteacher.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ArticleDatabaseDao {

    @Insert
    fun insert(article: Article)

    @Update
    fun update(article: Article)

    @Query("SELECT * from articles_table WHERE articleId = :key")
    fun get(key: Long): Article?

    @Query("SELECT * from articles_table WHERE articleId=:key")
    fun getArticleWithId(key:Long): LiveData<Article>

    @Query("DELETE FROM articles_table")
    fun clear()

    @Query("SELECT * FROM articles_table ORDER BY articleId DESC")
    fun getAllArticles(): LiveData<List<Article>>

    @Query("SELECT * FROM articles_table ORDER BY articleId DESC LIMIT 1")
    fun getArticle(): Article?



}
