package com.owllife.youtubebookmark.data.pref

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.owllife.youtubebookmark.domain.SharedPref
import com.owllife.youtubebookmark.domain.firebase.MyProfileData
import com.owllife.youtubebookmark.entity.EntityConstants

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
        val str = info?.let {
            Gson().toJson(info)
        }
        sharedPrefWrapper.putString(getSharedPref(), USER_INFO, str)
    }

    override fun getUserInfo(): MyProfileData? {
        val str: String? = sharedPrefWrapper.getString(getSharedPref(), USER_INFO)
        return if (str.isNullOrEmpty()) {
            null
        } else Gson().fromJson(str, MyProfileData::class.java)
    }

    override fun setViewType(type: Int) {
        sharedPrefWrapper.putInt(getSharedPref(), VIEW_TYPE, type)
    }

    override fun getViewType(): Int {
        return sharedPrefWrapper.getInt(getSharedPref(), VIEW_TYPE, EntityConstants.VIEW_TYPE_FULL)
    }

    companion object {
        private const val USER_INFO = "user_info"
        private const val VIEW_TYPE = "view_type"

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
