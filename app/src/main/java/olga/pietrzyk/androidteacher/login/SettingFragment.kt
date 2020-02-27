package olga.pietrzyk.androidteacher.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import olga.pietrzyk.androidteacher.R

class SettingsFragment : PreferenceFragmentCompat() {

    companion object {
        const val TAG = "SettingsFragment"
    }


    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController =findNavController()
        viewModel.authenticationState.observe(viewLifecycleOwner, Observer{authenticationState ->
            when(authenticationState){
                LoginViewModel.AuthenticationState.AUTHENTICATED -> Log.i(TAG,"Authenticated")
                LoginViewModel.AuthenticationState.UNAUTHENTICATED -> navController.navigate(R.id.loginFragment)
                else -> Log.e(TAG, "New $authenticationState state tahat do not require any change")
            }

        })
    }
}