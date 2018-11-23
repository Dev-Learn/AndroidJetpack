package nam.tran.flatform.local

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

import javax.inject.Inject
import javax.inject.Singleton

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@Singleton
class Preference @Inject
internal constructor(mApp: Application) : IPreference {

    private val mPref: SharedPreferences

    init {
        mPref = mApp.getSharedPreferences(SHARED_REFERENCE_NAME, Context.MODE_PRIVATE)
    }

    companion object {

        /**
         * normal configurations
         */
        private val SHARED_REFERENCE_NAME = "Alarm Timer Config"
        const val TOKEN = "token"
    }

    override fun saveToken(token: String) {
        mPref.edit().putString(TOKEN,token).apply()
    }

    override fun getToken(): String {
        return mPref.getString(TOKEN,"")
    }
}
