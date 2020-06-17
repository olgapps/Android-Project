package olga.pietrzyk.androidteacher.indexedCards

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class IndexedCardsViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IndexedCardsViewModel::class.java)) {
            return IndexedCardsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
