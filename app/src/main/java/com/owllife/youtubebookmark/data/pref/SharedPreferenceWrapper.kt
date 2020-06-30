package com.owllife.youtubebookmark.data.pref

import android.content.SharedPreferences

/**
 * @author owllife.dev
 * @since 20. 6. 17
 */
interface SharedPreferenceWrapper {

    fun putBoolean(pref: SharedPreferences?, key: String?, data: Boolean)

    fun putString(pref: SharedPreferences?, key: String?, data: String?)

    fun putInt(pref: SharedPreferences?, key: String?, data: Int)

    fun putLong(pref: SharedPreferences?, key: String?, data: Long)

    fun getBoolean(pref: SharedPreferences?, key: String?): Boolean

    fun getBoolean(pref: SharedPreferences?, key: String?, defaultVal: Boolean): Boolean

    fun getString(pref: SharedPreferences?, key: String?): String?

    fun getString(pref: SharedPreferences?, key: String?, defVale: String?): String?

    fun getInt(pref: SharedPreferences?, key: String?): Int

    fun getInt(pref: SharedPreferences?, key: String?, defValue: Int): Int

    fun getLong(pref: SharedPreferences?, key: String?): Long

    fun getLong(pref: SharedPreferences?, key: String?, defValue: Long): Long
}
