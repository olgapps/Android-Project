package olga.pietrzyk.androidteacher

import android.content.Context

val PREFERENCE_LANGUAGE= "LoginCount"


class LanguagePreference(context: Context?) {
    val preference = context!!.getSharedPreferences(PREFERENCE_LANGUAGE, Context.MODE_PRIVATE)
    
    fun getLanguage():String?{
        return preference.getString(PREFERENCE_LANGUAGE,"en")
    }
    
    fun setLanguage(language: String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_LANGUAGE, language)
        editor.apply()
    }
}
