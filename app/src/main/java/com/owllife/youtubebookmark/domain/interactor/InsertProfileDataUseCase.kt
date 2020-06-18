package com.owllife.youtubebookmark.domain.interactor

import com.google.firebase.auth.FirebaseAuth
import com.owllife.youtubebookmark.core.interactor.UseCase
import com.owllife.youtubebookmark.domain.ProfileRepository
import com.owllife.youtubebookmark.domain.ResultData

/**
 * @author owllife.dev
 * @since 20. 6. 18
 */
class InsertProfileDataUseCase(private val profileRepository: ProfileRepository) :
    UseCase<Boolean, Unit>() {

    override suspend fun run(params: Unit): ResultData<Boolean> {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            return ResultData.Failure(Exception("firebase auth's current user is null"))
        }
        return profileRepository.insertProfileDataIfNotExists(auth.currentUser!!)
    }
}