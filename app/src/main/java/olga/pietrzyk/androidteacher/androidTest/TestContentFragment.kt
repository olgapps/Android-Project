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


        viewModel= ViewModelProviders.of(this).get(TestContentViewModel::class.java)

        binding.txtquestionNumber.text=viewModel.questionNumber.toString()
        binding.test=viewModel

        var idIndex = -1

        viewModel.questionNumber.observe(viewLifecycleOwner, Observer { newQuestionNumber ->
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



                if (idIndex!=-1 && viewModel.answers.value!![idIndex]== viewModel.currentQuestion.value!!.answers[0]) {
                    viewModel.increaseNumberOfCorrectAnswers()
                }


                if((viewModel.indexOfQuestion==(viewModel.numberOfQuestions-1)) && (idIndex!=-1)){
                    viewModel.setFinalResult()
                    val action =
                        TestContentFragmentDirections.actionTestContentFragmentToTestResultFragment(
                            viewModel.finalResult.value!!.toFloat()
                        )
                   NavHostFragment.findNavController(this).navigate(action)
                }

                if(idIndex!=-1 && viewModel.indexOfQuestion<(viewModel.numberOfQuestions-1)) {
                    viewModel.goToNextQuestion()

                    binding.radioGroup.clearCheck();
                    if(viewModel.indexOfQuestion<(viewModel.numberOfQuestions)){
                        viewModel.questionNumber.observe(viewLifecycleOwner, Observer { newQuestionNumber ->
                            binding.txtquestionNumber.text=newQuestionNumber.toString()
                        })

                    }
                    binding.invalidateAll()

                }
            }

        return binding.root
    }


}
