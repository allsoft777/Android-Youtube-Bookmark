package com.owllife.youtubebookmark.domain

import com.google.firebase.auth.FirebaseUser
import com.owllife.youtubebookmark.domain.firebase.MyProfileData

/**
 * @author owllife.dev
 * @since 20. 6. 17
 */
interface ProfileRepository {

    suspend fun insertProfileDataIfNotExists(firebaseUser: FirebaseUser): ResultData<Boolean>

    suspend fun fetchMyProfileData(firebaseUser: FirebaseUser): ResultData<MyProfileData>
}