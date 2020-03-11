package olga.pietrzyk.androidteacher.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.preference.PreferenceManager
import olga.pietrzyk.androidteacher.R
import kotlin.random.Random


class LoginViewModel : ViewModel() {



    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val authenticationState = FirebaseUserLiveData().map {user ->
        if(user!=null){
            AuthenticationState.AUTHENTICATED
        }else{
            AuthenticationState.UNAUTHENTICATED
        }
    }

}
