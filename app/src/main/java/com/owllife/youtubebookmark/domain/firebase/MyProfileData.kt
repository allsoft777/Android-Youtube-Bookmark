package com.owllife.youtubebookmark.domain.firebase

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import com.owllife.youtubebookmark.domain.firebase.FirestoreTable.UserProfile

/**
 * @author owllife.dev
 * @since 20. 6. 17
 */
class MyProfileData(

    @get:PropertyName(UserProfile.PHOTO_URL)
    @set:PropertyName(UserProfile.PHOTO_URL)
    var photoUrl: String = "",

    @get:PropertyName(UserProfile.DISPLAY_NAME)
    @set:PropertyName(UserProfile.DISPLAY_NAME)
    var displayName: String = "",

    @get:PropertyName(UserProfile.EMAIL)
    @set:PropertyName(UserProfile.EMAIL)
    var email: String? = null,

    @ServerTimestamp
    @get:PropertyName(UserProfile.CREATED_TS)
    @set:PropertyName(UserProfile.CREATED_TS)
    var createdTs: Timestamp? = null,

    @get:PropertyName(UserProfile.MEMBERSHIP_LEVEL)
    @set:PropertyName(UserProfile.MEMBERSHIP_LEVEL)
    var membershipLevel: Int = 0
)