package com.owllife.youtubebookmark.presentation.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.databinding.ActivityLoginBinding
import com.owllife.youtubebookmark.injection.ViewModelFactory
import com.owllife.youtubebookmark.presentation.common.BaseActivity
import com.owllife.youtubebookmark.presentation.common.BaseViewModel
import com.owllife.youtubebookmark.presentation.data.FinishScreenData

/**
 * @author owllife.dev
 * @since 20. 6. 17
 */
class LoginActivity : BaseActivity() {

    private val REQ_CODE_SIGN_IN = 1

    private lateinit var dataBinding: ActivityLoginBinding
    private var viewModel: LoginViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        dataBinding.lifecycleOwner = this
        lazyInitViewModel()
        bindProfileData()
        bindSigninBtn()
    }

    override fun getBaseViewModel(): BaseViewModel? {
        if (viewModel == null) {
            viewModel = ViewModelProvider(
                this,
                ViewModelFactory(this, application)
            ).get(LoginViewModel::class.java)
        }
        return viewModel
    }

    override fun onStart() {
        super.onStart()
        viewModel?.fetchProfileData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQ_CODE_SIGN_IN) {
            viewModel?.fireBaseAuthWithGoogle(data, this)
            return
        }
        return super.onActivityResult(requestCode, resultCode, data)
    }

    private fun lazyInitViewModel() {
        viewModel?.loadGoogleSignInClient(this)
    }

    private fun bindProfileData() {
        viewModel?.myProfileData?.observe(this, Observer {
            if (it == null || it.email.isNullOrEmpty()) {
                dataBinding.loginTriggerContainer.visibility = View.VISIBLE
            } else {
                viewModel!!.finishScreen(FinishScreenData.WithData(Activity.RESULT_OK))
            }
        })
    }

    private fun bindSigninBtn() {
        dataBinding.googleAccountBtn.setOnClickListener {
            viewModel?.let {
                val signInIntent = viewModel?.getGoogleSigninIntent()
                startActivityForResult(signInIntent, REQ_CODE_SIGN_IN)
            }
        }

        dataBinding.icClose.setOnClickListener{
            finish()
        }
    }
}