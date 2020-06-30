package com.owllife.youtubebookmark.domain

import com.owllife.youtubebookmark.domain.firebase.MyProfileData

/**
 * @author owllife.dev
 * @since 20. 6. 17
 */
interface SharedPref {

    fun setUserInfo(info: MyProfileData?)
    fun getUserInfo(): MyProfileData?

    fun setViewType(type: Int)
    fun getViewType(): Int
}