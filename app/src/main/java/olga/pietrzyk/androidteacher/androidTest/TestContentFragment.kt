package olga.pietrzyk.androidteacher.androidTest


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import olga.pietrzyk.androidteacher.R
//import olga.pietrzyk.androidteacher.TestContentFragmentDirections
import olga.pietrzyk.androidteacher.databinding.FragmentTestContentBinding


class TestContentFragment : Fragment() {




    private lateinit var viewModel : TestContentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = DataBindingUtil.inflate<FragmentTestContentBinding>(inflater,
            R.layout.fragment_test_content, container, false)
        //(activity as AppCompatActivity).supportActionBar?.title = "Test question"

        viewModel= ViewModelProviders.of(this).get(TestContentViewModel::class.java)

        binding.txtquestionNumber.text=viewModel.questionNumber.toString()
        binding.test=viewModel


        //var numberOfQuestionToBeAnswered=numberOfQuestions
        var idIndex = -1
        //binding.test=this
        viewModel.questionNumber.observe(this, Observer { newQuestionNumber ->
            //binding.wordText.text = newWord
            binding.txtquestionNumber.text=newQuestionNumber.toString()
        })



            binding.answerButton.setOnClickListener { view: View ->



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


                //dodaje ilość poprawnych odp (idzie do view)


                if (idIndex!=-1 && viewModel.answers.value!![idIndex]== viewModel.currentQuestion.value!!.answers[0]) {
                    //numberOfCorrectAnswers += 1
                    viewModel.increaseNumberOfCorrectAnswers()
                }

                //vin statement
                if((viewModel.indexOfQuestion==(viewModel.numberOfQuestions-1)) && (idIndex!=-1)){
                    viewModel.setFinalResult()
                    //finalResult=(numberOfCorrectAnswers.toDouble()*100/numberOfQuestions.toDouble())
                    val action =
                        TestContentFragmentDirections.actionTestContentFragmentToTestResultFragment(
                            viewModel.finalResult.value!!.toFloat()
                        )
                   NavHostFragment.findNavController(this).navigate(action)
                }

                if(idIndex!=-1 && viewModel.indexOfQuestion<(viewModel.numberOfQuestions-1)) {
                    viewModel.goToNextQuestion()
                    /*numberOfQuestionToBeAnswered -= 1
                    indexOfQuestion += 1
                    questionNumber +=1
                    //idIndex = -1
                    currentQuestion = questions[indexOfQuestion]
                    answers = currentQuestion.answers.toMutableList()
                    answers.shuffle()*/

                    binding.radioGroup.clearCheck();
                    if(viewModel.indexOfQuestion<(viewModel.numberOfQuestions)){
                        viewModel.questionNumber.observe(this, Observer { newQuestionNumber ->
                            //binding.wordText.text = newWord
                            binding.txtquestionNumber.text=newQuestionNumber.toString()
                        })

                    }
                    binding.invalidateAll()

                }



            }

        return binding.root
    }


}
