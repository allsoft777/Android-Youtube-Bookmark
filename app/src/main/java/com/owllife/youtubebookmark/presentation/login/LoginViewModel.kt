package com.owllife.youtubebookmark.presentation.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.data.logger.Logger
import com.owllife.youtubebookmark.domain.ResultData
import com.owllife.youtubebookmark.domain.SharedPref
import com.owllife.youtubebookmark.domain.firebase.MyProfileData
import com.owllife.youtubebookmark.domain.interactor.FetchMyProfileUseCase
import com.owllife.youtubebookmark.domain.interactor.InsertProfileDataUseCase
import com.owllife.youtubebookmark.domain.makeFailureData
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
    private val sharedPref: SharedPref,
    private val fetchMyProfileUseCase: FetchMyProfileUseCase,
    private val insertProfileDataUseCase: InsertProfileDataUseCase
) : BaseViewModel(appContext) {

    private val TAG: String = LoginViewModel::class.java.simpleName

    private var auth: FirebaseAuth? = null
    private var googleSignInClient: GoogleSignInClient? = null
    var myProfileData: MutableLiveData<MyProfileData> = MutableLiveData()

    fun loadGoogleSignInClient(activity: Activity) {
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(appContext.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    fun getGoogleSigninIntent(): Intent {
        return googleSignInClient!!.signInIntent
    }

    fun fetchProfileData() {
        Logger.d(TAG, "fetchProfileData")
        if (auth!!.currentUser == null) {
            Logger.v(TAG, "fetchProfileData - auth.getCurrentUser is null")
            handleDataLoading(false)
            myProfileData.value = null
            return
        }

        val userInfoVO: MyProfileData? = sharedPref.getUserInfo()
        if (userInfoVO != null && !TextUtils.isEmpty(userInfoVO.email)) {
            Logger.d(TAG, "fetchProfileData - already cached.")
            handleDataLoading(false)
            myProfileData.value = userInfoVO
            return
        }
        handleDataLoading(true)

        CoroutineScope(Dispatchers.Main).launch {
            val value = fetchMyProfileUseCase.execute(Unit)
            if (value is ResultData.Success) {
                handleDataLoading(false)
                sharedPref.setUserInfo(value.data)
                myProfileData.value = value.data
                setToastText(getString(R.string.msg_login_successfully))
            } else if (value is ResultData.Failure) {
                handleDataLoading(false)
                handleFailure(value)
            }
        }
    }

    fun fireBaseAuthWithGoogle(data: Intent?, activity: Activity?) {
        Logger.d(TAG, "fireBaseAuthWithGoogle")
        handleDataLoading(true)
        val task =
            GoogleSignIn.getSignedInAccountFromIntent(data)
        val account: GoogleSignInAccount?
        account = try {
            task.getResult(ApiException::class.java)
        } catch (e: ApiException) {
            Logger.e(TAG, "fireBaseAuthWithGoogle - " + e.message)
            handleDataLoading(false)
            handleFailure(ResultData.Failure(e as Exception))
            return
        }
        if (account == null) {
            Logger.e(TAG, "fireBaseAuthWithGoogle - account is null.")
            handleFailure(makeFailureData("Failed to retrieve a GoogleSignInAccount"))
            handleDataLoading(false)
            return
        }
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(activity!!, OnCompleteListener { paramTask: Task<AuthResult?> ->
                if (paramTask.isSuccessful) {
                    Logger.d(TAG, "fireBaseAuthWithGoogle - paramTask is successful")
                    uploadUserInfoToRemoteDb()
                    return@OnCompleteListener
                }
                Logger.e(TAG, "fireBaseAuthWithGoogle - paramTask is failure")
                handleDataLoading(false)
                handleFailure(ResultData.Failure(task.exception as Exception))
            })
    }

    private fun uploadUserInfoToRemoteDb() {
        CoroutineScope(Dispatchers.Main).launch {
            val value = insertProfileDataUseCase.execute(Unit)
            if (value is ResultData.Success) {
                fetchProfileData()
            } else if (value is ResultData.Failure) {
                handleDataLoading(false)
                handleFailure(value)
            }
        }
    }
}