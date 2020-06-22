package com.owllife.youtubebookmark.presentation.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.domain.ResultData
import com.owllife.youtubebookmark.domain.exception.ExceptionData
import com.owllife.youtubebookmark.domain.firebase.MyProfileData
import com.owllife.youtubebookmark.domain.interactor.FetchMyProfileUseCase
import com.owllife.youtubebookmark.domain.interactor.InsertProfileDataUseCase
import com.owllife.youtubebookmark.domain.interactor.SignInWithGoogleParams
import com.owllife.youtubebookmark.domain.interactor.SignInWithGoogleUseCase
import com.owllife.youtubebookmark.presentation.common.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author owllife.dev
 * @since 20. 6. 17
 */
@Suppress("PrivatePropertyName")
class LoginViewModel(
    private val appContext: Context,
    private val fetchMyProfileUseCase: FetchMyProfileUseCase,
    private val insertProfileDataUseCase: InsertProfileDataUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase
) : BaseViewModel(appContext) {

    companion object {
        val TAG: String = LoginViewModel::class.java.simpleName
    }

    private var auth: FirebaseAuth? = null
    private var googleSignInClient: GoogleSignInClient? = null

    private var _profileData: MutableLiveData<MyProfileData> = MutableLiveData()
    var profileData: LiveData<MyProfileData> = _profileData

    fun loadGoogleSignInClient(activity: Activity) {
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(appContext.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    fun getGoogleSignInIntent(): Intent {
        return googleSignInClient!!.signInIntent
    }

    fun loadProfile() {
        handleDataLoading(true)
        CoroutineScope(Dispatchers.Main).launch {
            val value = fetchMyProfileUseCase.execute(Unit)
            handleDataLoading(false)
            if (value is ResultData.Success) {
                _profileData.value = value.data
                setToastText(getString(R.string.msg_sign_in_successfully))
            } else if (value is ResultData.Failure) {
                if (value.exception is ExceptionData.NoErrException) {
                    _profileData.value = null
                } else {
                    handleFailure(value)
                }
            }
        }
    }

    fun fireBaseAuthWithGoogle(data: Intent?, activity: Activity?) {
        handleDataLoading(true)
        CoroutineScope(Dispatchers.Main).launch {
            val params = SignInWithGoogleParams(data, activity)
            val ret = signInWithGoogleUseCase.execute(params)
            if (ret is ResultData.Success) {
                uploadUserInfoToRemoteDb()
            } else {
                handleDataLoading(false)
                handleFailure(ret as ResultData.Failure)
            }
        }
    }

    private fun uploadUserInfoToRemoteDb() {
        CoroutineScope(Dispatchers.Main).launch {
            val value = insertProfileDataUseCase.execute(Unit)
            if (value is ResultData.Success) {
                loadProfile()
            } else if (value is ResultData.Failure) {
                handleDataLoading(false)
                handleFailure(value)
            }
        }
    }
}