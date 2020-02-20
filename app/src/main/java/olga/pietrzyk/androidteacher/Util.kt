package olga.pietrzyk.androidteacher

import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import olga.pietrzyk.androidteacher.database.Article

fun formatArticles(articles: List<Article>, resources: Resources): Spanned {
    val sb = StringBuilder()
    sb.apply {
        append(resources.getString(R.string.title))
        articles.forEach {
            append("<br>")
            append(resources.getString(R.string.article_title))
            append("\t${it.aticleTitle}<br>")
        }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

fun formatArticle(article:  Article, resources: Resources): Spanned {
    val sb = StringBuilder()
    sb.apply {
        append(resources.getString(R.string.title))
        article.apply {
            append("<br>")
            append(resources.getString(R.string.article_title))
            append("\t${article.aticleTitle}<br>")
        }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

class TextItemViewHolder(val textView: TextView): RecyclerView.ViewHolder(textView)
//starter view holder
/*
directing text view in a text view holder
couldnt recycler view just use a text diectly?
provide a lot of functionality
recycler view relies upon this functionality to correct the position of the views
as the list scroll
animating views
a view holder describes an item view and metadata about its place within a recycler view
view holders tell recycler view where and how an item should get drawn on the list
recylcer view will use item view when sectorally binding an item to display on the screen
recycler view does not care what kind of view is starting itemview
you can put enything you want
there is on method you might provide -> your view holder can tell recycle view what its id is
when you override get item id  reccyler view can use this id when performin animations
view holder is a KEY PART OF HOW RECYCLE VIEW ACTUALLY DRAWS ANIMATES AND SCROLLS YOUR LIST
if it just use a regular views it wouldnt be able to keep track of where on screen there wre displayed and not able to naimate changes
* */