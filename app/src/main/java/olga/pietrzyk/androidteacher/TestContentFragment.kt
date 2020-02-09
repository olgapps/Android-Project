package olga.pietrzyk.androidteacher


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import olga.pietrzyk.androidteacher.databinding.FragmentTestContentBinding


class TestContentFragment : Fragment() {
    data class TestQuestion(
        val question: String,
        val answers: List<String>
    )

    val questions: MutableList<TestQuestion> = mutableListOf(
        TestQuestion(question="What is the not a function of life cycle of android activity?",
            answers=listOf("onRecreate","onStart()","onResume()","onCreate()")),
        TestQuestion(question="Where you inflate your menu",
            answers=listOf("onCreateOptions","onOptionItemSelected","setHasOptionMenu","title")),
        TestQuestion(question="Wher UI Fragments contain a layout and occupy a place within?",
            answers=listOf("in the Activity Layout","in onContextView","in contect","in onCreate method")),
        TestQuestion(question="What id the most current version of Android",
            answers=listOf("1","2","3","4"))
    )

    lateinit var currentQuestion: TestQuestion
    lateinit var answers: MutableList<String>
    val numberOfQuestions = 3
    var numberOfCorrectAnswers=0
    var finalResult:Double=0.0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = DataBindingUtil.inflate<FragmentTestContentBinding>(inflater, R.layout.fragment_test_content, container, false)
        var indexOfQuestion=0

        var numberOfQuestionToBeAnswered=numberOfQuestions
        var idIndex = -1
        binding.test=this

        currentQuestion = questions[indexOfQuestion]
        answers = currentQuestion.answers.toMutableList()


            binding.answerButton.setOnClickListener { view: View ->
                var userAnswerId = binding.radioGroup.checkedRadioButtonId



                if (userAnswerId == R.id.btnAAnswer) {
                    idIndex = 0
                } else if (userAnswerId == R.id.btnBAnswer) {
                    idIndex = 1
                } else if (userAnswerId == R.id.btnCAnswer) {
                    idIndex = 2
                } else if (userAnswerId == R.id.btnDAnswer) {
                    idIndex = 3
                }

                if (idIndex == 0) {
                    numberOfCorrectAnswers += 1
                }
                if(idIndex!=-1) {
                    numberOfQuestionToBeAnswered -= 1
                    indexOfQuestion += 1

                    binding.invalidateAll()
                }

                if(numberOfQuestionToBeAnswered==0){
                    finalResult=(numberOfCorrectAnswers.toDouble()*100/numberOfQuestions.toDouble())
                    Log.i("AAAAAAARESULT","L poprawnych odp ${numberOfCorrectAnswers} l odp to ${numberOfQuestions} wiec twój wynik to${finalResult}")
                    val action = TestContentFragmentDirections.actionTestContentFragmentToTestResultFragment(finalResult.toFloat())

                    NavHostFragment.findNavController(this).navigate(action)



                    // Navigation.findNavController(view).navigate(R.id.action_testContentFragment_to_testResultFragment)
                }

                currentQuestion = questions[indexOfQuestion]
                answers = currentQuestion.answers.toMutableList()
                idIndex = -1

            }





        return binding.root
        //return inflater.inflate(R.layout.fragment_test_content, container, false)
    }


}
