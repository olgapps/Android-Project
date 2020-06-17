package olga.pietrzyk.androidteacher.indexedCards

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import olga.pietrzyk.androidteacher.R

class IndexedCardsViewModel(val applicaton: Application) : AndroidViewModel(applicaton) {
    data class IndexedCards(
        var definition: String,
        var description: String
    )

    private var _cards: MutableList<IndexedCards> = mutableListOf(
        IndexedCards(
            definition = applicaton.getString(R.string.definition1),
            description = applicaton.getString(R.string.description1)
        ),
        IndexedCards(
            definition = applicaton.getString(R.string.definition2),
            description = applicaton.getString(R.string.description2)
        ),
        IndexedCards(
            definition = applicaton.getString(R.string.definition3),
            description = applicaton.getString(R.string.description3)
        )
    )
    private var cardIndex = 0
    private val indexCardChanger = 1
    private val firstIndexCard = 0
    private var _meaning = MutableLiveData<Boolean>()
    val meaning: LiveData<Boolean>
        get() = _meaning

    private val _current_card = MutableLiveData<IndexedCards>()
    val current_card: LiveData<IndexedCards>
        get() = _current_card

    init {
        _cards.shuffle()
        _current_card.value = _cards[cardIndex]
        _meaning.value = false
    }

    private fun changeTheCard() {
        cardIndex += indexCardChanger
        _current_card.value = _cards[cardIndex]
    }

    fun showMeaning() {
        _meaning.value = true
    }

    private fun coverMeaning() {
        _meaning.value = false
    }

    fun changeCard() {
        changeTheCard()
        setWordsAgain()
        coverMeaning()
    }

    private fun setWordsAgain() {
        if (cardIndex == (_cards.size - indexCardChanger)) {
            cardIndex = firstIndexCard
            _cards.shuffle()
            _current_card.value = _cards[cardIndex]
        }
    }
}
