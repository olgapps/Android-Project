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
       // val fm = ((AppCompatActivity)context).startActivity().supportFragmentManager
        val pioneersFragment = UpdateArticleDialogFragment.newInstance(article.id.toString())


        pioneersFragment.show(fm, "UpdateArticleDialogFragment_tag")



//        val builder = AlertDialog.Builder(adapterContext)
//        val inflater =LayoutInflater.from(adapterContext)
//        val view = inflater.inflate(R.layout.update_article, null)
//
//        val articleTitle= view.findViewById<TextView>(R.id.article_title)
//        val articleContent = view.findViewById<TextView>(R.id.article_content)
//
//        builder.apply {
//           setTitle(context.getResources().getString(R.string.update_article))
//
//            setView(view)
//
//            setPositiveButton(
//                context.getResources().getString(R.string.update),
//                object : DialogInterface.OnClickListener {
//                    override fun onClick(dialog: DialogInterface?, which: Int) {
//                        val articleInDatabase =
//                            FirebaseDatabase.getInstance().getReference("articles")
//                        val title = articleTitle.text.toString()
//                        val content = articleContent.text.toString()
//                        val article =
//                            Articles(article.id, title, content, MainFragment.currentUserMail)
//                        articleInDatabase.child(article.id.toString()).setValue(article)
//                        Toast.makeText(
//                            adapterContext,
//                            context.getResources().getString(R.string.updated_article),
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//                })
//
//            setNegativeButton(
//                context.getResources().getString(R.string.no),
//                object : DialogInterface.OnClickListener {
//                    override fun onClick(dialog: DialogInterface?, which: Int) {
//                        Toast.makeText(
//                            adapterContext,
//                            context.getResources().getString(R.string.no_updated_article),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                })
//        }
//
//        val alert = builder.create()
//        alert.show()
    }




}