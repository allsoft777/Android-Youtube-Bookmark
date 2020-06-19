package com.owllife.youtubebookmark.domain.interactor

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.owllife.youtubebookmark.core.interactor.UseCase
import com.owllife.youtubebookmark.data.logger.Logger
import com.owllife.youtubebookmark.domain.ResultData
import com.owllife.youtubebookmark.domain.makeFailureData
import com.owllife.youtubebookmark.presentation.login.LoginViewModel
import kotlinx.coroutines.tasks.await

/**
 * @author owllife.dev
 * @since 20. 6. 19
 */
class SignInWithGoogleUseCase : UseCase<Unit, SignInWithGoogleParams>() {

    override suspend fun run(params: SignInWithGoogleParams): ResultData<Unit> {
        val task = GoogleSignIn.getSignedInAccountFromIntent(params.intent)
        val account: GoogleSignInAccount = try {
            task.getResult(ApiException::class.java)
        } catch (e: ApiException) {
            Logger.e(LoginViewModel.TAG, "fireBaseAuthWithGoogle - " + e.message)
            return ResultData.Failure(e as Exception)
        } ?: return makeFailureData("fireBaseAuthWithGoogle - account is null.")

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        val auth = FirebaseAuth.getInstance()

        return try {
            auth.signInWithCredential(credential).await()
            ResultData.Success(Unit)
        } catch (e: Exception) {
            ResultData.Failure(task.exception as Exception)
        }
    }
}