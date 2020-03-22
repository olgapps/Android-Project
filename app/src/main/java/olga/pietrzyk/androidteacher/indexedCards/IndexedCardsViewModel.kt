package olga.pietrzyk.androidteacher.indexedCards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IndexedCardsViewModel: ViewModel(){
    data class IndexedCards(
        var definition: String,
        var description: String
    )
    private var _cards: MutableList<IndexedCards> = mutableListOf(
        IndexedCards(
            definition = "Geotagging",
            description = "Wherein in your phone finds your location via GPS and attaches coordinates to pictures you're taking. Can be a privacy/security concern."
        ),
        IndexedCards(
            definition = "Kindle",
            description = "Amazon's popular tablets and e Readers. Also an app for Android."
        ),
        IndexedCards(
            definition = "Launcher",
            description = "Collectively, the part of the Android user interface on home screens that lets you launch apps, make phone calls, etc. Is built in to Android, or can be purchased in the Android Market"
        )
    )
    private var cardIndex = 0
    private val indexCardChanger = 1
    private val firstIndexCard = 0
    private var _meaning= MutableLiveData <Boolean>()
    val meaning: LiveData<Boolean>
        get() = _meaning

    private val _current_card= MutableLiveData<IndexedCards>()
    val current_card: LiveData<IndexedCards>
        get() = _current_card

    init{
        _cards.shuffle()
        _current_card.value =_cards[cardIndex]
        _meaning.value = false
    }

    private fun changeTheCard(){
        cardIndex += indexCardChanger
        _current_card.value =_cards[cardIndex]
    }

    fun showMeaning(){
        _meaning.value = true
    }

    private fun coverMeaning(){
        _meaning.value = false
    }

    fun changeCard(){
        changeTheCard()
        setWordsAgain()
        coverMeaning()
    }

    private fun setWordsAgain(){
        if(cardIndex == (_cards.size-indexCardChanger)){
            cardIndex = firstIndexCard
            _cards.shuffle()
            _current_card.value=_cards[cardIndex]
        }
    }
}
