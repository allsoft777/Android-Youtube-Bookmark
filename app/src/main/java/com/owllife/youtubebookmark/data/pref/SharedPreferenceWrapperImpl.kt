package com.owllife.youtubebookmark.data.pref

import android.annotation.SuppressLint
import android.content.SharedPreferences

/**
 * @author owllife.dev
 * @since 20. 6. 17
 */
@SuppressLint("ApplySharedPref")
class SharedPreferenceWrapperImpl : SharedPreferenceWrapper {

    override fun putBoolean(
        pref: SharedPreferences?,
        key: String?,
        data: Boolean
    ) {
        pref?.apply {
            val editors = edit()
            editors.putBoolean(key, data)
            editors.commit()
        }
    }

    override fun putString(pref: SharedPreferences?, key: String?, data: String?) {
        pref?.apply {
            val editors = edit()
            editors.putString(key, data)
            editors.commit()
        }
    }

    override fun putInt(pref: SharedPreferences?, key: String?, data: Int) {
        pref?.apply {
            val editors = edit()
            editors.putInt(key, data)
            editors.commit()
        }
    }

    override fun putLong(pref: SharedPreferences?, key: String?, data: Long) {
        pref?.apply {
            val editors = edit()
            editors.putLong(key, data)
            editors.commit()
        }
    }

    override fun getBoolean(pref: SharedPreferences?, key: String?): Boolean {
        return pref != null && pref.getBoolean(key, false)
    }

    override fun getBoolean(
        pref: SharedPreferences?,
        key: String?,
        defaultVal: Boolean
    ): Boolean {
        return pref != null && pref.getBoolean(key, defaultVal)
    }

    override fun getString(pref: SharedPreferences?, key: String?): String {
        return (if (pref == null) "" else pref.getString(key, ""))!!
    }

    override fun getString(
        pref: SharedPreferences?,
        key: String?,
        defVale: String?
    ): String? {
        return if (pref == null) defVale else pref.getString(key, defVale)
    }

    override fun getInt(pref: SharedPreferences?, key: String?): Int {
        return pref?.getInt(key, -1) ?: -1
    }

    override fun getInt(pref: SharedPreferences?, key: String?, defValue: Int): Int {
        return pref?.getInt(key, defValue) ?: defValue
    }

    override fun getLong(pref: SharedPreferences?, key: String?): Long {
        return pref?.getLong(key, -1) ?: -1
    }

    override fun getLong(pref: SharedPreferences?, key: String?, defValue: Long): Long {
        return pref?.getLong(key, defValue) ?: -1
    }

    companion object {
        private var sInstance: SharedPreferenceWrapper? = null

        @get:Synchronized
        val instance: SharedPreferenceWrapper?
            get() {
                if (sInstance == null) {
                    sInstance = SharedPreferenceWrapperImpl()
                }
                return sInstance
            }
    }
}
