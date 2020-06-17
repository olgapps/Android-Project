package olga.pietrzyk.androidteacher.login

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.database.FirebaseDatabase
import olga.pietrzyk.androidteacher.R

class UpdateArticleDialogFragment : DialogFragment() {
    private var articleId: String = "-1"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        articleId = arguments?.getString(ID)!!.toString()
    }

    companion object {
        private const val ID = "ID"
        fun newInstance(
            articleId: String
        ): UpdateArticleDialogFragment = UpdateArticleDialogFragment().apply {
            arguments = Bundle().apply {
                putString(ID, articleId)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = LayoutInflater.from(it)
            val view = inflater.inflate(R.layout.dialog_fragment_update_article, null)
            val articleTitle = view!!.findViewById<TextView>(R.id.article_title)
            val articleContent = view.findViewById<TextView>(R.id.article_content)

            builder.setView(view)
            builder.setPositiveButton(
                it.resources.getString(R.string.update)
            ) { _, _ ->
                val articleInDatabase =
                    FirebaseDatabase.getInstance().getReference("articles")
                val title = articleTitle.text.toString()
                val content = articleContent.text.toString()
                val article = Articles(articleId, title, content, ArticleFragment.currentUserMail)
                articleInDatabase.child(article.id.toString()).setValue(article)
                Toast.makeText(
                    it,
                    it.resources.getString(R.string.updated_article),
                    Toast.LENGTH_LONG
                ).show()
            }

            builder.setNegativeButton(
                it.getResources().getString(R.string.no)
            ) { _, _ ->
                Toast.makeText(
                    it,
                    it.resources.getString(R.string.no_updated_article),
                    Toast.LENGTH_SHORT
                ).show()
            }
            builder.create()
        } ?: throw IllegalStateException(resources.getString(R.string.cannot_be_null))
    }
}
