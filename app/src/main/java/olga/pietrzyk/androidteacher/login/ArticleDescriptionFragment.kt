package olga.pietrzyk.androidteacher.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import olga.pietrzyk.androidteacher.R
import olga.pietrzyk.androidteacher.databinding.FragmentArticleDescriptionBinding

class ArticleDescriptionFragment : Fragment() {
    private lateinit var binding: FragmentArticleDescriptionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_article_description,
            container,
            false
        )
        val argMail =
            ArticleDescriptionFragmentArgs.fromBundle(
                arguments!!
            )
        binding.articleContent.text = argMail.articleContent
        binding.articleTitle.text = argMail.articleTitle
        val ref = FirebaseDatabase.getInstance().reference

        if (argMail.articleEmail == ArticleFragment.currentUserMail && ArticleFragment.currentUserMail != "null") {
            binding.deleteButton.visibility = View.VISIBLE
        }

        val applesQuery = ref.child("articles").orderByChild("id").equalTo(argMail.articleKey)
        binding.deleteButton.setOnClickListener { view: View ->
            applesQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (appleSnapshot in dataSnapshot.children) {
                        appleSnapshot.ref.removeValue()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("error", "onCancelled", databaseError.toException())
                }
            })
            view.findNavController()
                .navigate(R.id.action_articleDescriptionFragment_to_mainFragment)
        }
        return binding.root
    }
}
