package olga.pietrzyk.androidteacher.login

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import olga.pietrzyk.androidteacher.R

class ArticlesAdapter(
    private val adapterContext: Context,
    val layoutResId: Int,
    private val articlesList: List<Articles>
) : ArrayAdapter<Articles>(adapterContext, layoutResId, articlesList) {
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(adapterContext)
        val view: View = layoutInflater.inflate(layoutResId, null)
        val articleField = view.findViewById<TextView>(R.id.article_title)
        val updateArticle = view.findViewById<ImageView>(R.id.update_article)
        val article = articlesList[position]
        articleField.text = article.title
        if (article.email != ArticleFragment.currentUserMail) {
            updateArticle.visibility = View.GONE
        }

        updateArticle.setOnClickListener {
            showUpdateDialog(article)
        }
        return view;
    }

    private fun showUpdateDialog(article: Articles) {
        val fragmentManager = (adapterContext as AppCompatActivity).supportFragmentManager
        val articlesDialogFragment = UpdateArticleDialogFragment.newInstance(article.id.toString())
        articlesDialogFragment.show(fragmentManager, "UpdateArticleDialogFragment_tag")

    }
}