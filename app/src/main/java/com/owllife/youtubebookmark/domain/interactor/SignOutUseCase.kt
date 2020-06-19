package com.owllife.youtubebookmark.domain.interactor

import com.google.firebase.auth.FirebaseAuth
import com.owllife.youtubebookmark.core.interactor.UseCase
import com.owllife.youtubebookmark.domain.ResultData
import com.owllife.youtubebookmark.domain.SharedPref

/**
 * @author owllife.dev
 * @since 20. 6. 19
 */
class SignOutUseCase(
    private val sharedPref: SharedPref
) : UseCase<Unit, Unit>() {

    override suspend fun run(params: Unit): ResultData<Unit> {
        FirebaseAuth.getInstance().signOut()
        sharedPref.setUserInfo(null)
        return ResultData.Success(Unit)
    }
}