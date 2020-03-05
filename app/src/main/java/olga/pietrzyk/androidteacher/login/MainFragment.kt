package olga.pietrzyk.androidteacher.login


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import olga.pietrzyk.androidteacher.MainViewFragmentDirections

import olga.pietrzyk.androidteacher.R
import olga.pietrzyk.androidteacher.databinding.FragmentLoginBinding
import olga.pietrzyk.androidteacher.databinding.FragmentMainBinding


/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {
    lateinit var referenceToFirebase: DatabaseReference
    lateinit var articlesList: MutableList<Articles>
    lateinit var articleTitle: MutableList<String>
    lateinit var itemKeys: MutableList<String>
    //lateinit var adapterIn: ArticlesAdapter
    //lateinit var adapterOut: ArrayAdapter<String>



    companion object {
        lateinit var currentUserMail: String
        const val TAG = "MainFragment"
        const val SIGN_IN_RESULT_CODE = 1001
    }



    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)


        referenceToFirebase =FirebaseDatabase.getInstance().getReference("articles")

        currentUserMail= FirebaseAuth.getInstance().currentUser?.email.toString()

        //Toast.makeText(context,"'${currentUserMail}'", Toast.LENGTH_LONG).show()




        articlesList= mutableListOf()
        articleTitle= mutableListOf()

//        binding.welcomeText.text ="tutaj będzie lista Artykułow"
//        binding.authButton.text = getString(R.string.login_btn)
        binding.btnSubmitArticle.setOnClickListener { saveArticle() }




        referenceToFirebase.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
               if(p0!!.exists()){
                   var i:Long=0
                    articlesList.clear()
                       for (p in p0.children) {

                               val article = p.getValue(Articles::class.java)
                                   articlesList.add(article!!)


                   }


                    articleTitle.clear()
                   for(a in articlesList){
                       articleTitle.add(a.title)
                   }

                   context?.let{

                       val adapterLoggedIn = ArticlesAdapter(it, R.layout.articles, articlesList)
                       val adapterLoggedOut =ArrayAdapter(it, android.R.layout.simple_list_item_1, articleTitle)



                       if(currentUserMail=="null"){
                           binding.listView.adapter=adapterLoggedOut
                       }else{
                           binding.listView.adapter=adapterLoggedIn
                       }



                      /* viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenicationState ->
                           when(authenicationState){
                               LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                                   binding.listView.adapter=adapterLoggedIn

                               }
                               else -> {
                                   binding.listView.adapter=adapterLoggedOut
                               }
                           }
                       })*/
                   }
               }
            }
        });

        binding.listView.setOnItemClickListener { parent, view, position, id ->

            var articleContent = articlesList[id.toInt()].content.toString()
            var articleTitle = articlesList[id.toInt()].title.toString()
            var articleKey = articlesList[id.toInt()].id.toString()
            var articleEmail = articlesList[id.toInt()].email.toString()
            view.findNavController().navigate(MainFragmentDirections.actionMainFragmentToArticleDescriptionFragment( articleTitle, articleContent,articleKey, articleEmail))

        }
        binding.invalidateAll()
        return binding.root
    }



    fun saveArticle(){
        val articleTitle = binding.articleTitle.text.toString()
        val articleContent = binding.articleContent.text.toString()
        val title = articleTitle
        val content= articleContent


        val Id=referenceToFirebase.push().key
        val articleId=Id.toString()
        val article = Articles(articleId, title, content, currentUserMail)




        referenceToFirebase.child(articleId).setValue(article).addOnCompleteListener{
            Toast.makeText(context, getString(R.string.created_article), Toast.LENGTH_LONG).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAuthenticationState()

        binding.authButton.setOnClickListener {
            launchSignInFlow()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESULT_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {

                Log.i(TAG, "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!")
            } else {
                Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }

    private fun observeAuthenticationState() {
        //val factToDisplay = viewModel.getFactToDisplay(requireContext())
        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenicationState ->
            context?.let {

            when (authenicationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    binding.articleContent.visibility=View.VISIBLE
                    binding.articleTitle.visibility=View.VISIBLE
                    binding.btnSubmitArticle.visibility=View.VISIBLE
                    binding.authButton.text = getString(R.string.logout_button_text)
                    binding.authButton.setOnClickListener {
                        AuthUI.getInstance().signOut(requireContext())
                    }
                    binding.welcomeText.text=getFactWithPersonalization(":)")
                    binding.textView2.visibility=View.GONE
                    val adapterLoggedIn = ArticlesAdapter(it, R.layout.articles, articlesList)
                    binding.listView.adapter=adapterLoggedIn
                    currentUserMail= FirebaseAuth.getInstance().currentUser?.email.toString()

                }
                else -> {
                    binding.articleContent.visibility=View.GONE
                    binding.articleTitle.visibility=View.GONE
                    binding.btnSubmitArticle.visibility=View.GONE
                    binding.authButton.text = getString(R.string.login_button_text)
                    binding.textView2.text="to add your articles login ->"
                    binding.authButton.setOnClickListener { launchSignInFlow() }
                    //binding.welcomeText.text=factToDisplay
                    binding.welcomeText.text="Articles list:"
                    val adapterLoggedOut =ArrayAdapter(it, android.R.layout.simple_list_item_1, articleTitle)
                    binding.listView.adapter=adapterLoggedOut
                    currentUserMail="null"
                    //binding.welcomeText.text ="tutaj będzie lista Artykułow"
                    binding.authButton.text = getString(R.string.login_btn)

                }

            }


            }
        })

    }


    private fun getFactWithPersonalization(fact: String): String {
        return String.format(
            resources.getString(
                R.string.welcome_message_authed,
                FirebaseAuth.getInstance().currentUser?.displayName
            )
        )
    }

    private fun launchSignInFlow() {
        val providers = arrayListOf(

            AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build())

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            MainFragment.SIGN_IN_RESULT_CODE
        )
    }
}


