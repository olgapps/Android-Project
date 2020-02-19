package olga.pietrzyk.androidteacher.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="articles_table")
data class Article(
    @PrimaryKey(autoGenerate=true)
    var articleId: Long=0L,

    @ColumnInfo(name = "title")
    val aticleTitle: String?,

    @ColumnInfo(name = "content")
    var articleContent: String?,

    @ColumnInfo(name = "quality_rating")
    var articleRate: Int = -1

)