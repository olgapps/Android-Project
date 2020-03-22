package olga.pietrzyk.androidteacher.androidTest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import olga.pietrzyk.androidteacher.R

class TestContentViewModel(val applicaton: Application) : AndroidViewModel(applicaton) {

    data class TestQuestion(
        val question: String,
        val answers: List<String>
    )

    private val questions: MutableList<TestQuestion> = mutableListOf(
        TestQuestion(
            question = applicaton.getString(R.string.question1),
            answers = listOf(
                applicaton.getString(R.string.answer1a),
                applicaton.getString(R.string.answer1b),
                applicaton.getString(R.string.answer1c),
                applicaton.getString(R.string.answer1d)
            )
        ),
        TestQuestion(
            question = applicaton.getString(R.string.question2),
            answers = listOf(
                applicaton.getString(R.string.answer2a),
                applicaton.getString(R.string.answer2b),
                applicaton.getString(R.string.answer2c),
                applicaton.getString(R.string.answer2d)
            )
        ),
        TestQuestion(
            question = applicaton.getString(R.string.question3),
            answers = listOf(
                applicaton.getString(R.string.answer3a),
                applicaton.getString(R.string.answer3b),
                applicaton.getString(R.string.answer3c),
                applicaton.getString(R.string.answer3d)
            )
        ),
        TestQuestion(
            question = applicaton.getString(R.string.question4),
            answers = listOf(
                applicaton.getString(R.string.answer4a),
                applicaton.getString(R.string.answer4b),
                applicaton.getString(R.string.answer4c),
                applicaton.getString(R.string.answer4d)
            )
        )
    )

    private val initialFinalResultValue = 0.0
    private val initialNumberOfCorrectAnswers = 0
    private val initialQuestionNumber = 1
    private val fullPercentage = 100
    private val increaseOfIndexQuestion = 1
    private val increaseOfCorrectAnswer = 1
    var currentQuestion = MutableLiveData<TestQuestion>()
    var answers = MutableLiveData<MutableList<String>>()
    val numberOfQuestions = questions.size
    var numberOfCorrectAnswers = MutableLiveData<Int>()
    var finalResult = MutableLiveData<Double>()
    var questionNumber = MutableLiveData<Int>()
    var indexOfQuestion = 0

    init {
        finalResult.value = initialFinalResultValue
        questions.shuffle()
        numberOfCorrectAnswers.value = initialNumberOfCorrectAnswers
        currentQuestion.value = questions[indexOfQuestion]
        val currentQuestionValue = currentQuestion.value
        questionNumber.value = initialQuestionNumber
        answers.value = currentQuestionValue?.answers?.toMutableList()
        answers.value?.shuffle()
    }

    fun increaseNumberOfCorrectAnswers() {
        numberOfCorrectAnswers.value = (numberOfCorrectAnswers.value)?.plus(increaseOfCorrectAnswer)
    }

    fun goToNextQuestion() {
        indexOfQuestion += increaseOfIndexQuestion
        questionNumber.value = (questionNumber.value)?.plus(initialQuestionNumber)
        currentQuestion.value = questions[indexOfQuestion]
        val currentQuestionValue = currentQuestion.value
        answers.value = currentQuestionValue?.answers?.toMutableList()
        answers.value?.shuffle()
    }

    fun setFinalResult() {
        finalResult.value =
            (numberOfCorrectAnswers.value?.toDouble()!!.times(fullPercentage) / numberOfQuestions.toDouble())
    }
}