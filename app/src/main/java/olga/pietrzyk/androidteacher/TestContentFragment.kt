package olga.pietrzyk.androidteacher


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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
            answers=listOf("onRecreate()","onStart()","onResume()","onCreate()")),
        TestQuestion(question="Where you inflate your menu",
            answers=listOf("onCreateOptions","onOptionItemSelected","setHasOptionMenu","onInflateMenu")),
        TestQuestion(question="Where UI Fragments contain a layout and occupy a place within?",
            answers=listOf("in the Activity Layout","in onContextView","in context","in onCreate method")),
        TestQuestion(question="What id the most current version of Android",
            answers=listOf("Android Pie","Android Oreo","Android Nougat","Android Marshmallow"))
    )

    lateinit var currentQuestion: TestQuestion
    lateinit var answers: MutableList<String>
    val numberOfQuestions = questions.size
    var numberOfCorrectAnswers=0
    var finalResult:Double=0.0
    var questionNumber=1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = DataBindingUtil.inflate<FragmentTestContentBinding>(inflater, R.layout.fragment_test_content, container, false)
        //(activity as AppCompatActivity).supportActionBar?.title = "Test question"
        binding.txtquestionNumber.text=questionNumber.toString()

        var indexOfQuestion=0
        var numberOfQuestionToBeAnswered=numberOfQuestions
        var idIndex = -1
        binding.test=this

        questions.shuffle()


        currentQuestion = questions[indexOfQuestion]
        answers = currentQuestion.answers.toMutableList()
        answers.shuffle()

            binding.answerButton.setOnClickListener { view: View ->
            //jeden problem z numerowaniem


                var userAnswerId = binding.radioGroup.checkedRadioButtonId
                idIndex=(-1)

                if (userAnswerId == R.id.btnAAnswer) {
                    idIndex = 0
                } else if (userAnswerId == R.id.btnBAnswer) {
                    idIndex = 1
                } else if (userAnswerId == R.id.btnCAnswer) {
                    idIndex = 2
                } else if (userAnswerId == R.id.btnDAnswer) {
                    idIndex = 3
                }

                if (idIndex!=-1 && answers[idIndex] == currentQuestion.answers[0]) {
                    numberOfCorrectAnswers += 1
                }

                if((indexOfQuestion==(numberOfQuestions-1)) && (idIndex!=-1)){
                    finalResult=(numberOfCorrectAnswers.toDouble()*100/numberOfQuestions.toDouble())
                    Log.i("AAAAAAARESULT","L poprawnych odp ${numberOfCorrectAnswers} l odp to ${numberOfQuestions} wiec twój wynik to${finalResult}")
                    val action = TestContentFragmentDirections.actionTestContentFragmentToTestResultFragment(finalResult.toFloat())
                    NavHostFragment.findNavController(this).navigate(action)
                }

                if(idIndex!=-1 && indexOfQuestion<(numberOfQuestions-1)) {
                    numberOfQuestionToBeAnswered -= 1
                    indexOfQuestion += 1
                    questionNumber +=1
                    //idIndex = -1
                    currentQuestion = questions[indexOfQuestion]
                    answers = currentQuestion.answers.toMutableList()
                    answers.shuffle()

                    binding.radioGroup.clearCheck();
                    if(indexOfQuestion<(numberOfQuestions)){
                        binding.txtquestionNumber.text=questionNumber.toString()
                    }
                    binding.invalidateAll()

            }



            }

        return binding.root
    }


}
