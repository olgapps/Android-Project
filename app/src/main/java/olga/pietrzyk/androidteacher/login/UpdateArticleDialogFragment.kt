package olga.pietrzyk.androidteacher.login

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.database.FirebaseDatabase
import olga.pietrzyk.androidteacher.R
import olga.pietrzyk.androidteacher.tasks.TaskDialogFragment

class UpdateArticleDialogFragment : DialogFragment() {



    private var articleId: String ="-1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        articleId = arguments?.getString(ID)!!.toString()
    }


    companion object {
        private const val ID= "ID"

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
            val view = inflater.inflate(R.layout.update_article, null)


            val articleTitle= view!!.findViewById<TextView>(R.id.article_title)
            val articleContent = view!!.findViewById<TextView>(R.id.article_content)


            builder.setTitle(it.getResources().getString(R.string.update_article))

            builder.setView(view)

            builder.setPositiveButton(
                    it.getResources().getString(R.string.update),
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            val articleInDatabase =
                                FirebaseDatabase.getInstance().getReference("articles")
                            val title = articleTitle.text.toString()
                            val content = articleContent.text.toString()
                            val article =  Articles(articleId, title, content, MainFragment.currentUserMail)
                           articleInDatabase.child(article.id.toString()).setValue(article)
                            Toast.makeText(
                                it,
                                it.getResources().getString(R.string.updated_article),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })

            builder.setNegativeButton(
                    it.getResources().getString(R.string.no),
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            Toast.makeText(
                                it,
                                it.getResources().getString(R.string.no_updated_article),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })


            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
