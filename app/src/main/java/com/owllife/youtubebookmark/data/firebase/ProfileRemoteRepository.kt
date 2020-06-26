package com.owllife.youtubebookmark.data.firebase

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.owllife.youtubebookmark.data.logger.MainLogger
import com.owllife.youtubebookmark.domain.ProfileRepository
import com.owllife.youtubebookmark.domain.ResultData
import com.owllife.youtubebookmark.domain.firebase.FirestoreTable.UserProfile
import com.owllife.youtubebookmark.domain.firebase.MyProfileData
import com.owllife.youtubebookmark.domain.makeFailureData
import kotlinx.coroutines.tasks.await
import java.util.*

/**
 * @author owllife.dev
 * @since 20. 6. 17
 */
class ProfileRemoteRepository : ProfileRepository {

    @Suppress("PrivatePropertyName")
    private val TAG = ProfileRemoteRepository::class.java.simpleName

    override suspend fun insertProfileDataIfNotExists(firebaseUser: FirebaseUser): ResultData<Boolean> {
        val eventDocument = FirebaseFirestore.getInstance()
            .collection(UserProfile.COLLECTION_NAME)
            .document(firebaseUser.uid)

        return try {
            val ret = eventDocument.get().await()
            if (ret != null && ret.exists()) {
                MainLogger.d(TAG, "uid is already exists. : " + firebaseUser.uid)
                ResultData.Success(true)
            } else {
                MainLogger.d(TAG, "there is no uid. need to add data")
                insertUserInfoToRemote(firebaseUser)
            }
        } catch (e: java.lang.Exception) {
            ResultData.Failure(e)
        }
    }

    override suspend fun fetchMyProfileData(firebaseUser: FirebaseUser): ResultData<MyProfileData> {
        val eventDocument = FirebaseFirestore.getInstance()
            .collection(UserProfile.COLLECTION_NAME)
            .document(firebaseUser.uid)

        return try {
            val ret = eventDocument.get().await()
            if (ret == null) {
                makeFailureData("failed to fetch the profile data from Remote")
            } else {
                val profileData: MyProfileData = ret.toObject(MyProfileData::class.java)!!
                ResultData.Success(profileData)
            }
        } catch (e: Exception) {
            ResultData.Failure(e)
        }
    }

    private suspend fun insertUserInfoToRemote(firebaseUser: FirebaseUser): ResultData<Boolean> {
        val obj: MutableMap<String, Any?> = HashMap()
        obj[UserProfile.DISPLAY_NAME] = firebaseUser.displayName
        obj[UserProfile.EMAIL] = firebaseUser.email
        val photoUrl =
            if (firebaseUser.photoUrl == null) "" else firebaseUser.photoUrl.toString()
        obj[UserProfile.PHOTO_URL] = photoUrl
        obj[UserProfile.CREATED_TS] = FieldValue.serverTimestamp()

        val eventDocument = FirebaseFirestore.getInstance()
            .collection(UserProfile.COLLECTION_NAME)
            .document(firebaseUser.uid)

        return try {
            eventDocument.set(obj).await()
            ResultData.Success(true)
        } catch (e: java.lang.Exception) {
            ResultData.Failure(e)
        }
    }
}