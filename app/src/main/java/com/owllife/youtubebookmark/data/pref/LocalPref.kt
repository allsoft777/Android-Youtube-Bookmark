package com.owllife.youtubebookmark.data.pref

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.owllife.youtubebookmark.domain.SharedPref
import com.owllife.youtubebookmark.domain.firebase.MyProfileData

/**
 * @author owllife.dev
 * @since 20. 6. 17
 */
class SharedPreferenceRepository(
    private val appContext: Context,
    private val sharedPrefWrapper: SharedPreferenceWrapper
) : SharedPref {

    private fun getSharedPref(): SharedPreferences {
        return appContext.getSharedPreferences("youtube-bookmark", Activity.MODE_PRIVATE)
    }

    override fun setUserInfo(info: MyProfileData?) {
        var str = ""
        if (info != null) {
            val gson = Gson()
            str = gson.toJson(info)
        }
        sharedPrefWrapper.putString(
            getSharedPref(),
            USER_INFO,
            str
        )
    }

    override fun getUserInfo(): MyProfileData? {
        val str: String? = sharedPrefWrapper.getString(getSharedPref(), USER_INFO)
        return if (str.isNullOrEmpty()) {
            null
        } else Gson().fromJson(str, MyProfileData::class.java)
    }

    companion object {
        private const val USER_INFO = "user_info"
        private var sInstance: SharedPref? = null

        @Synchronized
        fun getInstance(
            appContext: Context,
            sharedPrefWrapper: SharedPreferenceWrapper
        ): SharedPref {
            if (sInstance == null) {
                sInstance =
                    SharedPreferenceRepository(appContext, sharedPrefWrapper)
            }
            return sInstance!!
        }
    }
}
