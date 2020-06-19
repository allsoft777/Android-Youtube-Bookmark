package com.owllife.youtubebookmark.domain.interactor

import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import com.owllife.youtubebookmark.core.interactor.UseCase
import com.owllife.youtubebookmark.data.logger.Logger
import com.owllife.youtubebookmark.domain.ProfileRepository
import com.owllife.youtubebookmark.domain.ResultData
import com.owllife.youtubebookmark.domain.SharedPref
import com.owllife.youtubebookmark.domain.exception.ExceptionData
import com.owllife.youtubebookmark.domain.firebase.MyProfileData

/**
 * @author owllife.dev
 * @since 20. 6. 18
 */
class FetchMyProfileUseCase(
    private val sharedPref: SharedPref,
    private val profileRepository: ProfileRepository
) :
    UseCase<MyProfileData, Unit>() {

    companion object {
        val TAG = FetchMyProfileUseCase::class.java.simpleName
    }

    override suspend fun run(params: Unit): ResultData<MyProfileData> {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            val errMsg = "Firebase auth's current user is null"
            return ResultData.Failure(ExceptionData.NoErrException(errMsg))
        }

        val userInfoVO: MyProfileData? = sharedPref.getUserInfo()
        if (userInfoVO != null && !TextUtils.isEmpty(userInfoVO.email)) {
            Logger.d(TAG, "fetchProfileData - already cached.")
            return ResultData.Success(userInfoVO)
        }

        val ret = profileRepository.fetchMyProfileData(auth.currentUser!!)
        if (ret is ResultData.Success) {
            sharedPref.setUserInfo(ret.data)
        }
        return ret
    }
}