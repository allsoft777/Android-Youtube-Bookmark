package com.owllife.youtubebookmark.domain.firebase

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import com.owllife.youtubebookmark.domain.firebase.FirestoreTable.UserProfile
import kotlinx.android.parcel.Parcelize

/**
 * @author owllife.dev
 * @since 20. 6. 17
 */
@Parcelize
data class MyProfileData(
    @PropertyName(value = UserProfile.PHOTO_URL)
    var photoUrl: String? = null,

    @PropertyName(value = UserProfile.DISPLAY_NAME)
    var displayName: String? = null,

    @PropertyName(value = UserProfile.EMAIL)
    var email: String? = null,

    @ServerTimestamp
    @PropertyName(value = UserProfile.CREATED_TS)
    var createdTs: Timestamp? = null,

    @PropertyName(value = UserProfile.MEMBERSHIP_LEVEL)
    var membershipLevel: Int = 0
) : Parcelable