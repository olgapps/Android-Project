package olga.pietrzyk.androidteacher.login


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import olga.pietrzyk.androidteacher.R

import olga.pietrzyk.androidteacher.databinding.FragmentMainBinding


class MainFragment : Fragment() {
    lateinit var referenceToFirebase: DatabaseReference
    lateinit var articlesList: MutableList<Articles>
    lateinit var articleTitle: MutableList<String>
    //lateinit var listOfArticles:
    lateinit var listOfArticles: MutableList<Articles>


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
        referenceToFirebase = FirebaseDatabase.getInstance().getReference("articles")

        currentUserMail = FirebaseAuth.getInstance().currentUser?.email.toString()

        articlesList = mutableListOf()
        articleTitle = mutableListOf()

        binding.btnSubmitArticle.setOnClickListener { saveArticle() }
        createArticleListFromFirebase()
        observeAuthenticationState()


        bindArticleItemWithList()
        handleListScrollingInsideScreenScrolling()
        return binding.root
    }

    private fun bindArticleItemWithList() {
        binding.listView.setOnItemClickListener { parent, view, position, id ->

            var articleContent = articlesList[id.toInt()].content.toString()
            var articleTitle = articlesList[id.toInt()].title.toString()
            var articleKey = articlesList[id.toInt()].id.toString()
            var articleEmail = articlesList[id.toInt()].email.toString()
            view.findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToArticleDescriptionFragment(
                    articleTitle,
                    articleContent,
                    articleKey,
                    articleEmail
                )
            )
        }
    }

    private fun createArticleListFromFirebase() {
        val adapterLoggedIn = ArticlesAdapter(context!!, R.layout.articles, articlesList)
        val adapterLoggedOut = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, articleTitle)
        referenceToFirebase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()) {
                    var i: Long = 0
                    articlesList.clear()
                    for (p in p0.children) {
                        val article = p.getValue(Articles::class.java)
                        articlesList.add(article!!)
                    }

                    articleTitle.clear()
                    for (a in articlesList) {
                        articleTitle.add(a.title)
                    }
                }



                if (currentUserMail == "null") {
                    binding.listView.adapter = adapterLoggedOut
                } else {
                    binding.listView.adapter = adapterLoggedIn
                }
            }
        });
    }

    fun saveArticle(){
        val articleTitle = binding.articleTitle.text.toString()
        val articleContent = binding.articleContent.text.toString()
        val title = articleTitle
        val content= articleContent


        val Id = referenceToFirebase.push().key

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
        context?.let {
            val adapterLoggedIn = ArticlesAdapter(it, R.layout.articles, articlesList)
            val adapterLoggedOut =
                ArrayAdapter(it, android.R.layout.simple_list_item_1, articleTitle)


        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenicationState ->

                when (authenicationState) {
                    LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                        binding.apply {
                            authButton.text = getString(R.string.logout_button_text)
                            authButton.setOnClickListener {
                                AuthUI.getInstance().signOut(requireContext())
                            }
                            welcomeText.text = getFactWithPersonalization(":)")
                            textView2.visibility = View.GONE
                            currentUserMail =
                                FirebaseAuth.getInstance().currentUser?.email.toString()
                            listView.adapter = adapterLoggedIn
                            articleContent.visibility = View.VISIBLE
                            articleTitle.visibility = View.VISIBLE
                            btnSubmitArticle.visibility = View.VISIBLE
                        }
                    }
                    else -> {
                        binding.apply {
                            articleContent.visibility = View.GONE
                            articleTitle.visibility = View.GONE
                            btnSubmitArticle.visibility = View.GONE
                            authButton.text = getString(R.string.login_button_text)
                            textView2.text = "to add your articles login ->"
                            authButton.setOnClickListener { launchSignInFlow() }
                            welcomeText.text = "Articles list:"
                            listView.adapter = adapterLoggedOut
                            currentUserMail = "null"
                            authButton.text = getString(R.string.login_btn)
                        }
                    }
                }
            })
        }
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

    fun handleListScrollingInsideScreenScrolling(){
        binding.listView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val action = event!!.action
                when (action) {
                    MotionEvent.ACTION_DOWN ->
                        v!!.parent.requestDisallowInterceptTouchEvent(true)

                    MotionEvent.ACTION_UP ->
                        v!!.parent.requestDisallowInterceptTouchEvent(false)
                }

                v!!.onTouchEvent(event)
                return true
            }
        })
    }
}


