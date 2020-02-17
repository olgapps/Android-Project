package olga.pietrzyk.androidteacher

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TestContentViewModel : ViewModel(){



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


    var currentQuestion=MutableLiveData<TestQuestion>()
    var answers= MutableLiveData<MutableList<String>>()
    val numberOfQuestions = questions.size
    var numberOfCorrectAnswers=MutableLiveData<Int>()
    var finalResult=MutableLiveData<Double>()
    var questionNumber=MutableLiveData<Int>()
    var indexOfQuestion=0

    init{
        finalResult.value=0.0
        questions.shuffle()
        numberOfCorrectAnswers.value=0
        currentQuestion.value = questions[indexOfQuestion]
        var k=currentQuestion.value
        questionNumber.value=1
        answers.value = k?.answers?.toMutableList()
        answers.value?.shuffle()
    }

    fun increaseNumberOfCorrectAnswers(){
        numberOfCorrectAnswers.value=(numberOfCorrectAnswers.value)?.plus(1)
    }

    fun goToNextQuestion(){

        indexOfQuestion += 1
        questionNumber.value=(questionNumber.value)?.plus(1)
        //idIndex = -1
        currentQuestion.value = questions[indexOfQuestion]
        var k=currentQuestion.value
        answers.value = k?.answers?.toMutableList()
        answers.value?.shuffle()

    }

    fun setFinalResult(){

        finalResult.value=(numberOfCorrectAnswers.value?.toDouble()!!.times(100)/numberOfQuestions.toDouble())

    }


}