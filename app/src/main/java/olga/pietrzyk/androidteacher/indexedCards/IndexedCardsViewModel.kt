package olga.pietrzyk.androidteacher.indexedCards

import android.view.View
import androidx.lifecycle.ViewModel
import olga.pietrzyk.androidteacher.TestContentFragment

class IndexedCardsViewModel: ViewModel(){

    data class IndexedCards(
        var definition: String,
        var description: String
    )

   private val _cards: MutableList<IndexedCards> = mutableListOf(
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
    var card: List<IndexedCards>
        get()=_cards



}