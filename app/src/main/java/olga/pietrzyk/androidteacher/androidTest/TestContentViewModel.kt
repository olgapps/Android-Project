package olga.pietrzyk.androidteacher.androidTest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TestContentViewModel : ViewModel(){

    data class TestQuestion(
        val question: String,
        val answers: List<String>
    )

    private val questions: MutableList<TestQuestion> = mutableListOf(
        TestQuestion(
            question = "What is the not a function of life cycle of android activity?",
            answers = listOf("onRecreate()", "onStart()", "onResume()", "onCreate()")
        ),
        TestQuestion(
            question = "Where you inflate your menu",
            answers = listOf(
                "onCreateOptions",
                "onOptionItemSelected",
                "setHasOptionMenu",
                "onInflateMenu"
            )
        ),
        TestQuestion(
            question = "Where UI Fragments contain a layout and occupy a place within?",
            answers = listOf(
                "in the Activity Layout",
                "in onContextView",
                "in context",
                "in onCreate method"
            )
        ),
        TestQuestion(
            question = "What id the most current version of Android",
            answers = listOf("Android Pie", "Android Oreo", "Android Nougat", "Android Marshmallow")
        )
    )

    private val initialFinalResultValue = 0.0
    private val initialNumberOfCorrectAnswers = 0
    private val initialQuestionNumber = 1
    private val fullPercentage = 100
    private val increaseOfIndexQuestion = 1
    private val increaseOfCorrectAnswer = 1
    var currentQuestion = MutableLiveData<TestQuestion>()
    var answers= MutableLiveData<MutableList<String>>()
    val numberOfQuestions = questions.size
    var numberOfCorrectAnswers=MutableLiveData<Int>()
    var finalResult = MutableLiveData<Double>()
    var questionNumber = MutableLiveData<Int>()
    var indexOfQuestion = 0

    init{
        finalResult.value = initialFinalResultValue
        questions.shuffle()
        numberOfCorrectAnswers.value = initialNumberOfCorrectAnswers
        currentQuestion.value = questions[indexOfQuestion]
        val currentQuestionValue = currentQuestion.value
        questionNumber.value=initialQuestionNumber
        answers.value = currentQuestionValue?.answers?.toMutableList()
        answers.value?.shuffle()
    }

    fun increaseNumberOfCorrectAnswers(){
        numberOfCorrectAnswers.value = (numberOfCorrectAnswers.value)?.plus(increaseOfCorrectAnswer)
    }

    fun goToNextQuestion(){
        indexOfQuestion += increaseOfIndexQuestion
        questionNumber.value = (questionNumber.value)?.plus(initialQuestionNumber)
        currentQuestion.value = questions[indexOfQuestion]
        val currentQuestionValue = currentQuestion.value
        answers.value = currentQuestionValue?.answers?.toMutableList()
        answers.value?.shuffle()
    }

    fun setFinalResult(){
        finalResult.value=(numberOfCorrectAnswers.value?.toDouble()!!.times(fullPercentage)/numberOfQuestions.toDouble())
    }
}