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




    //lateinit var adapterLoggedIn:  ArrayAdapter<Articles>
    //lateinit var adapterLoggedOut: ArrayAdapter<String>

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

        Log.i("AAAAAAAAAAAAAA","${currentUserMail}")




        articlesList= mutableListOf()
        articleTitle= mutableListOf()

        binding.welcomeText.text ="tutaj będzie lista Artykułow"
        binding.authButton.text = getString(R.string.login_btn)
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
                      // binding.listView.adapter=adapterLoggedIn

                       viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenicationState ->
                           when(authenicationState){
                               LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                                   binding.listView.adapter=adapterLoggedIn

                               }
                               else -> {
                                   binding.listView.adapter=adapterLoggedOut
                               }

                           }

                       })



                   }

               }
            }
        });
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
        Log.i("BBBBBBBBBB","${currentUserMail}")
        val article = Articles(articleId, title, content, currentUserMail)
        Log.i("BBBBBBBBBB","${article}")
        //!!!!!!!!!!!!!!!!!!!!!!!!!!1
        //val userArticle = UserArticle(articleId, title, content, currentUserMail)
        //val article2 = Articles(articleId, "title", "content")


        referenceToFirebase.child(articleId).setValue(article).addOnCompleteListener{
            Toast.makeText(context, "you created article", Toast.LENGTH_LONG).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAuthenticationState()

        binding.authButton.setOnClickListener {
            launchSignInFlow()
        }
        binding.settingsBtn.setOnClickListener {view:View->
            view.findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
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
                //val adapter : ArrayAdapter<Articles>
                        //= ArticlesAdapter(it, R.layout.articles, articlesList)
                val currentUserId= FirebaseAuth.getInstance().currentUser?.email

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

                    //binding.listView.adapter=adapterLoggedIn
                    //val adapter= ArticlesAdapter(it, R.layout.articles, articlesList)
                    //, referenceToFirebase, currentUserId
                    //binding.listView.adapter=adapter
                }
                else -> {
                    binding.articleContent.visibility=View.GONE
                    binding.articleTitle.visibility=View.GONE
                    binding.btnSubmitArticle.visibility=View.GONE
                    binding.authButton.text = getString(R.string.login_button_text)
                    binding.authButton.setOnClickListener { launchSignInFlow() }
                    //binding.welcomeText.text=factToDisplay
                    binding.welcomeText.text="tutaj będzie lista Artykułow a Ty się nie zalogowałeś"


                    //val adapter = ArrayAdapter(it, android.R.layout.simple_list_item_1, articleTitle)
                    //binding.listView.adapter=adapterLoggedOut
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


