package olga.pietrzyk.androidteacher.login

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.articles.view.*
import olga.pietrzyk.androidteacher.R

class ArticlesAdapter(val adapterContext: Context, val layoutResId: Int,val articlesList: List<Articles>): ArrayAdapter<Articles>(adapterContext, layoutResId, articlesList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(adapterContext)
        val view: View = layoutInflater.inflate(layoutResId, null)

        Log.i("aaaaaa", "${articlesList}")

        val articleField = view.findViewById<TextView>(R.id.article_field)
        val updateArticle = view.findViewById<TextView>(R.id.update_article)

        Log.i("aaaaaa", "${articleField}")
        val article =articlesList[position]
        Log.i("aaaaab", "${article.title}")
        articleField.text=article.title

        updateArticle.setOnClickListener {
            showUpdateDialog()
        }
        return view;
    }

    fun showUpdateDialog(){
        val builder = AlertDialog.Builder(adapterContext)

    }
}