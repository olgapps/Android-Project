package olga.pietrzyk.androidteacher.login


import android.annotation.SuppressLint
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
import olga.pietrzyk.androidteacher.databinding.FragmentArticlesBinding

class ArticleFragment : Fragment() {
    private lateinit var referenceToFirebase: DatabaseReference
    lateinit var articlesList: MutableList<Articles>
    lateinit var articleTitle: MutableList<String>

    lateinit var adapterLoggedIn: ArticlesAdapter
    lateinit var adapterLoggedOut: ArrayAdapter<*>

    companion object {
        lateinit var currentUserMail: String
        const val TAG = "ArticleFragment"
        const val SIGN_IN_RESULT_CODE = 1001
    }

    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding: FragmentArticlesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_articles, container, false)

        referenceToFirebase = FirebaseDatabase.getInstance().getReference("articles")
        currentUserMail = FirebaseAuth.getInstance().currentUser?.email.toString()

        articlesList = mutableListOf()
        articleTitle = mutableListOf()

        adapterLoggedIn = ArticlesAdapter(context!!, R.layout.list_item_article, articlesList)
        adapterLoggedOut = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, articleTitle)
        binding.btnSubmitArticle.setOnClickListener { saveArticle() }

        bindArticleItemWithList()
        handleListScrollingInsideScreenScrolling()
        return binding.root
    }

    private fun bindArticleItemWithList() {
        binding.listView.setOnItemClickListener { _, view, _, id ->
            val articleContent = articlesList[id.toInt()].content
            val articleTitle = articlesList[id.toInt()].title
            val articleKey = articlesList[id.toInt()].id.toString()
            val articleEmail = articlesList[id.toInt()].email
            view.findNavController().navigate(
                ArticleFragmentDirections.actionMainFragmentToArticleDescriptionFragment(
                    articleTitle,
                    articleContent,
                    articleKey,
                    articleEmail
                )
            )
        }
    }

    private fun createArticleListFromFirebase() {

        referenceToFirebase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
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
        })
    }

    private fun saveArticle(){
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
                Log.i(TAG, getString(R.string.sucesfully_signed_in) + "${FirebaseAuth.getInstance().currentUser?.displayName}!")
            } else {
                Log.i(TAG, getString(R.string.unsucesfully_signed_in) +"${response?.error?.errorCode}")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeAuthenticationState() {

        context?.let {
            createArticleListFromFirebase()
            viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenicationState ->
                when (authenicationState) {
                    LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                        binding.apply {
                            authButton.text = getString(R.string.logout_button_text)
                            authButton.setOnClickListener {
                                AuthUI.getInstance().signOut(requireContext())
                            }
                            welcomeText.text = getWelcomeData()
                            loginInstruction.visibility = View.GONE
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
                            loginInstruction.text = getString(R.string.add_article_text)
                            authButton.setOnClickListener { launchSignInFlow() }
                            welcomeText.text = getString(R.string.articles_list)
                            listView.adapter = adapterLoggedOut
                            currentUserMail =   resources.getString(
                                R.string.null_value)
                            authButton.text = getString(R.string.login_btn)
                        }
                    }
                }
            })
        }
    }

    private fun getWelcomeData(): String {
        return String.format(
            resources.getString(
                R.string.welcome_message_authed,
                FirebaseAuth.getInstance().currentUser?.displayName
            )
        )
    }

    private fun launchSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                providers
            ).build(), ArticleFragment.SIGN_IN_RESULT_CODE
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    fun handleListScrollingInsideScreenScrolling(){
        binding.listView.setOnTouchListener { v, event ->
            when (event!!.action) {
                MotionEvent.ACTION_DOWN ->
                    v!!.parent.requestDisallowInterceptTouchEvent(true)

                MotionEvent.ACTION_UP ->
                    v!!.parent.requestDisallowInterceptTouchEvent(false)
            }
            v!!.onTouchEvent(event)
            true
        }
    }
}


