package olga.pietrzyk.androidteacher


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import olga.pietrzyk.androidteacher.databinding.FragmentTestBinding
import olga.pietrzyk.androidteacher.databinding.FragmentTestContentBinding


class TestContentFragment : Fragment() {
    data class TestQuestion(
        val question: String,
        val answers: List<String>
    )

    val questions: MutableList<TestQuestion> = mutableListOf(
        TestQuestion(question="What id the most current version of Android",
            answers=listOf("1","2","3","4")),
        TestQuestion(question="Where you inflate your menu",
            answers=listOf("onCreateOptions","onOptionItemSelected","setHasOptionMenu","title")),
        TestQuestion(question="Wher UI Fragments contain a layout and occupy a place within?",
            answers=listOf("in the Activity Layout","in onContextView","in contect","in onCreate method")),
        TestQuestion(question="What id the most current version of Android",
            answers=listOf("1","2","3","4"))
    )

    lateinit var currentQuestion: TestQuestion
    lateinit var answers: MutableList<String>
    val numOfQuestions = 3

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = DataBindingUtil.inflate<FragmentTestContentBinding>(inflater, R.layout.fragment_test_content, container, false)

        currentQuestion=TestQuestion(question="What id the most current version of Android",
            answers=listOf("1","2","3","4"))
        answers=currentQuestion.answers.toMutableList()

        binding.test=this

        return binding.root
        //return inflater.inflate(R.layout.fragment_test_content, container, false)
    }


}
