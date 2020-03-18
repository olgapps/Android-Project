package olga.pietrzyk.androidteacher.login

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import olga.pietrzyk.androidteacher.R


class ArticlesAdapter(val adapterContext: Context, val layoutResId: Int, val articlesList: List<Articles>): ArrayAdapter<Articles>(adapterContext, layoutResId, articlesList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(adapterContext)
        val view: View = layoutInflater.inflate(layoutResId, null)
        val articleField = view.findViewById<TextView>(R.id.article_field)
        val updateArticle = view.findViewById<ImageView>(R.id.update_article)
        val article =articlesList[position]
        articleField.text=article.title

        if(article.email!=MainFragment.currentUserMail){
            updateArticle.visibility=View.GONE
        }

        updateArticle.setOnClickListener {
            showUpdateDialog(article)
        }
        return view;
    }

    fun showUpdateDialog(article: Articles){
        val inflater =LayoutInflater.from(adapterContext)
        val view = inflater.inflate(R.layout.update_article, null)

        val fm = (adapterContext as AppCompatActivity).supportFragmentManager
        val pioneersFragment = UpdateArticleDialogFragment.newInstance(article.id.toString())
        pioneersFragment.show(fm, "UpdateArticleDialogFragment_tag")

    }
}