package olga.pietrzyk.androidteacher

import android.content.Context

const val PREFERENCE_LANGUAGE= "Language"


class LanguagePreference(context: Context?) {
    private val preference = context!!.getSharedPreferences(PREFERENCE_LANGUAGE, Context.MODE_PRIVATE)
    
    fun getLanguage():String?{
        return preference.getString(PREFERENCE_LANGUAGE,"en")
    }
    
    fun setLanguage(language: String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_LANGUAGE, language)
        editor.apply()
    }
}
