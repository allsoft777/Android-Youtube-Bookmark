package com.owllife.youtubebookmark.presentation.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.core.showToastMsg
import com.owllife.youtubebookmark.databinding.ActivityLoginBinding
import com.owllife.youtubebookmark.injection.ViewModelFactory
import com.owllife.youtubebookmark.presentation.common.BaseActivity
import com.owllife.youtubebookmark.presentation.data.FinishScreenData

/**
 * @author owllife.dev
 * @since 20. 6. 17
 */
class LoginActivity : BaseActivity() {

    companion object {
        const val REQ_CODE_SIGN_IN = 1

        fun callingIntent(parentContext: Context) = run {
            val intent = Intent(parentContext, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
    }

    private lateinit var dataBinding: ActivityLoginBinding
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory(application)
        ).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        dataBinding.lifecycleOwner = this
        lazyInitViewModel()
        bindViewModelData()
        bindSignInBtn()
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadProfile()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQ_CODE_SIGN_IN) {
            viewModel.fireBaseAuthWithGoogle(data, this)
            return
        }
        return super.onActivityResult(requestCode, resultCode, data)
    }

    private fun lazyInitViewModel() {
        viewModel.loadGoogleSignInClient(this)
    }

    private fun bindViewModelData() {
        viewModel.let { vm ->
            vm.profileData.observe(this, Observer { profileData ->
                if (profileData == null || profileData.email.isNullOrEmpty()) {
                    dataBinding.loginTriggerContainer.visibility = View.VISIBLE
                } else {
                    viewModel.setFinishData(FinishScreenData.WithData(Activity.RESULT_OK))
                }
            })
            vm.dataLoading.observe(this, Observer { isLoading ->
                if (isLoading) loadingDialog.value.show() else loadingDialog.value.dismiss()
            })
            viewModel.toastText.observe(this, Observer { showToastMsg(it) })
            viewModel.failureData.observe(this, Observer { data ->
                data.exception.message?.let { msg -> showToastMsg(msg) }
            })
            viewModel.finishScreenData.observe(this, Observer {
                if (it is FinishScreenData.WithData) {
                    setResult(it.resultCode)
                }
                finish()
            })
        }
    }

    private fun bindSignInBtn() {
        dataBinding.googleAccountBtn.setOnClickListener {
            viewModel.let {
                val signInIntent = viewModel.getGoogleSignInIntent()
                startActivityForResult(signInIntent, REQ_CODE_SIGN_IN)
            }
        }

        dataBinding.icClose.setOnClickListener {
            finish()
        }
    }
}