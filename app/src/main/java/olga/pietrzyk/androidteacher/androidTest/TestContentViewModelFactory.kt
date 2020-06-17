package olga.pietrzyk.androidteacher.androidTest

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TestContentViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TestContentViewModel::class.java)) {
            return TestContentViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}