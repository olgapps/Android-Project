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
import olga.pietrzyk.androidteacher.databinding.FragmentTestContentBinding


class TestContentFragment : Fragment() {

    private lateinit var viewModel : TestContentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val indexOutOfRange =-1
        val decreaseForIndex = 1
        val decreaseForQuestionNumber = 1
        val firstItemIndex = 0
        val secondItemIndex = 1
        val thirdItemIndex = 2
        val fourthItemIndex = 3

        val binding = DataBindingUtil.inflate<FragmentTestContentBinding>(inflater,
            R.layout.fragment_test_content, container, false)

        viewModel= ViewModelProviders.of(this).get(TestContentViewModel::class.java)

        binding.questionNumber.text
        binding.test=viewModel

        viewModel.questionNumber.observe(viewLifecycleOwner, Observer { newQuestionNumber ->
            binding.questionContent.text=newQuestionNumber.toString()
        })
            binding.answerButton.setOnClickListener {

                val userAnswerId = binding.radioGroup.checkedRadioButtonId
                var idIndex = indexOutOfRange

                if (userAnswerId == R.id.btnAAnswer) {
                    idIndex = firstItemIndex
                } else if (userAnswerId == R.id.btnBAnswer) {
                    idIndex = secondItemIndex
                } else if (userAnswerId == R.id.btnCAnswer) {
                    idIndex = thirdItemIndex
                } else if (userAnswerId == R.id.btnDAnswer) {
                    idIndex =  fourthItemIndex
                }

                if (idIndex!=indexOutOfRange && viewModel.answers.value!![idIndex]== viewModel.currentQuestion.value!!.answers[firstItemIndex]) {
                    viewModel.increaseNumberOfCorrectAnswers()
                }

                if((viewModel.indexOfQuestion==(viewModel.numberOfQuestions-decreaseForIndex)) && (idIndex!=indexOutOfRange)){
                    viewModel.setFinalResult()
                    val action =
                        TestContentFragmentDirections.actionTestContentFragmentToTestResultFragment(
                            viewModel.finalResult.value!!.toFloat()
                        )
                   NavHostFragment.findNavController(this).navigate(action)
                }

                if(idIndex!=indexOutOfRange && viewModel.indexOfQuestion < (viewModel.numberOfQuestions-decreaseForQuestionNumber)) {
                    viewModel.goToNextQuestion()

                    binding.radioGroup.clearCheck();
                    if(viewModel.indexOfQuestion < (viewModel.numberOfQuestions)){
                        viewModel.questionNumber.observe(viewLifecycleOwner, Observer { newQuestionNumber ->
                            binding.questionNumber.text=newQuestionNumber.toString()
                        })
                    }
                    binding.invalidateAll()
                }
            }
        return binding.root
    }
}
