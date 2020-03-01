package olga.pietrzyk.androidteacher.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

       binding=DataBindingUtil.inflate(inflater, R.layout.fragment_article_description, container, false)
        //binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        // Inflate the layout for this fragment
        //binding.
        var argMail =
            ArticleDescriptionFragmentArgs.fromBundle(
                arguments!!
            )

        binding.articleContent.text=argMail.articleContent
        binding.articleTitle.text=argMail.articleTitle

        val ref = FirebaseDatabase.getInstance().reference

        if(argMail.articleEmail==MainFragment.currentUserMail && MainFragment.currentUserMail!="null"){
            binding.deleteButton.visibility=View.VISIBLE
        }
        //val mostafa = ref.child("articles").child(argMail.articleKey).child("id")
        //val mail = mostafa.child("email")

        //val article = ref.child("articles").child("id")

        //Toast.makeText(context,"${mail}",Toast.LENGTH_LONG).show()

        /*ref.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                val article = p0.getValue(Articles::class.java)
                Toast.makeText(context,"${article!!.email} a ${argMail.articleEmail}",Toast.LENGTH_LONG).show()
                if(article!!.email==argMail.articleEmail){
                    binding.deleteButton.visibility=View.VISIBLE
                }
            }
        })*/
        val applesQuery = ref.child("articles").orderByChild("id").equalTo(argMail.articleKey)
        //val emailQuery=ref.child("articles").order

        binding.deleteButton.setOnClickListener { view: View ->
//            binding.articleContent.text=""
//            binding.articleTitle.text=""

            applesQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {


                    for (appleSnapshot in dataSnapshot.children) {
                        appleSnapshot.ref.removeValue()
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("popo", "onCancelled", databaseError.toException())
                }
            })
            view.findNavController().navigate(R.id.action_articleDescriptionFragment_to_mainFragment)

        }

        return binding.root

        //return inflater.inflate(R.layout.fragment_article_description, container, false)
    }


}
